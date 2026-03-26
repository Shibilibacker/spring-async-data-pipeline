package com.asyncpipeline.spring_async_data_pipeline.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.asyncpipeline.spring_async_data_pipeline.dto.SubmitDataRequest;
import com.asyncpipeline.spring_async_data_pipeline.entity.DataRequest;
import com.asyncpipeline.spring_async_data_pipeline.repository.DataRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataProcessingService {

    private final DataRequestRepository dataRequestRepository;
    private final WebClient webClient;

    @Value("${queue.webhook-url}")
    private String webhookUrl;

    @Value("${cache.ttl-minutes}")
    private int cacheTtlMinutes;

    /**
     * Submit data request to queue (persist to database)
     */
    public DataRequest submitDataRequest(SubmitDataRequest request) {
        log.info("Submitting data request from user: {}", request.getEmail());

        DataRequest dataRequest = new DataRequest();
        dataRequest.setName(request.getName());
        dataRequest.setEmail(request.getEmail());
        dataRequest.setPayload(request.getPayload());

        DataRequest savedRequest = dataRequestRepository.save(dataRequest);
        log.info("Data request saved with ID: {}", savedRequest.getId());

        return savedRequest;
    }

    /**
     * Scheduled task to process queued requests and POST to webhook
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void processQueue() {
        log.info("Processing queue...");

        List<DataRequest> pendingRequests = dataRequestRepository
                .findByStatus(DataRequest.RequestStatus.PENDING);

        for (DataRequest request : pendingRequests) {
            processRequestAsync(request);
        }
    }

    /**
     * Async method to call webhook and update database
     */
    @Async("asyncExecutor")
    public void processRequestAsync(DataRequest request) {
        try {
            log.info("Processing request ID: {}", request.getId());
            request.setStatus(DataRequest.RequestStatus.PROCESSING);
            dataRequestRepository.save(request);

            // Call webhook asynchronously
            String response = callWebhook(request);

            request.setStatus(DataRequest.RequestStatus.COMPLETED);
            request.setWebhookResponse(response);
            request.setProcessedAt(LocalDateTime.now());

            dataRequestRepository.save(request);
            log.info("Request ID: {} processed successfully", request.getId());

            // Invalidate cache when data is updated
            evictDataCache(request.getId());

        } catch (Exception e) {
            log.error("Error processing request ID: {}", request.getId(), e);
            request.setStatus(DataRequest.RequestStatus.FAILED);
            dataRequestRepository.save(request);
        }
    }

    /**
     * Call external webhook asynchronously
     */
    private String callWebhook(DataRequest request) {
        try {
            return webClient.post()
                    .uri(webhookUrl)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to call webhook for request ID: {}", request.getId(), e);
            throw new RuntimeException("Webhook call failed", e);
        }
    }

    /**
     * Retrieve data from database with caching (TTL configured)
     */
    @Cacheable(value = "dataCache", key = "#id", unless = "#result == null")
    public DataRequest getDataById(Long id) {
        log.info("Fetching data for ID: {} from database", id);
        return dataRequestRepository.findById(id)
                .orElse(null);
    }

    /**
     * Evict cache entry when data is updated
     */
    @CacheEvict(value = "dataCache", key = "#id")
    public void evictDataCache(Long id) {
        log.info("Cache evicted for ID: {}", id);
    }
}