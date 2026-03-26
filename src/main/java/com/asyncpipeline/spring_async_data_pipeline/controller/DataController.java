package com.asyncpipeline.spring_async_data_pipeline.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.asyncpipeline.spring_async_data_pipeline.dto.ApiResponse;
import com.asyncpipeline.spring_async_data_pipeline.dto.DataResponse;
import com.asyncpipeline.spring_async_data_pipeline.dto.SubmitDataRequest;
import com.asyncpipeline.spring_async_data_pipeline.entity.DataRequest;
import com.asyncpipeline.spring_async_data_pipeline.exception.ResourceNotFoundException;
import com.asyncpipeline.spring_async_data_pipeline.service.DataProcessingService;

@RestController
@RequestMapping("/v1/data")
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataProcessingService dataProcessingService;

    /**
     * 1. Submit data request - adds to queue
     * Only accepts JSON content type
     */
    @PostMapping(value = "/submit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<DataResponse>> submitData(
            @Valid @RequestBody SubmitDataRequest request) {

        log.info("Received data submission request from: {}", request.getEmail());

        DataRequest savedRequest = dataProcessingService.submitDataRequest(request);
        DataResponse response = DataResponse.fromEntity(savedRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Data submitted successfully and queued for processing", response));
    }

    /**
     * 2. Retrieve data from database with caching
     * Second request will be retrieved from cache (TTL: 30 minutes)
     */
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<DataResponse>> getData(
            @PathVariable Long id) {

        log.info("Fetching data for ID: {}", id);

        DataRequest dataRequest = dataProcessingService.getDataById(id);

        if (dataRequest == null) {
            throw new ResourceNotFoundException("Data not found for ID: " + id);
        }

        DataResponse response = DataResponse.fromEntity(dataRequest);
        return ResponseEntity.ok(ApiResponse.ok("Data retrieved successfully", response));
    }

    /**
     * 3. Health check endpoint
     */
    @GetMapping(value = "/health",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.ok("Service is running", "OK"));
    }
}