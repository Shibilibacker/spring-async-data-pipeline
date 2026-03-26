package com.asyncpipeline.spring_async_data_pipeline.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.asyncpipeline.spring_async_data_pipeline.dto.SubmitDataRequest;
import com.asyncpipeline.spring_async_data_pipeline.entity.DataRequest;
import com.asyncpipeline.spring_async_data_pipeline.repository.DataRequestRepository;
import com.asyncpipeline.spring_async_data_pipeline.service.DataProcessingService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataProcessingServiceTest {

    @Mock
    private DataRequestRepository dataRequestRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private DataProcessingService dataProcessingService;

    private SubmitDataRequest submitRequest;
    private DataRequest dataRequest;

    @BeforeEach
    public void setUp() {
        submitRequest = new SubmitDataRequest();
        submitRequest.setName("John Doe");
        submitRequest.setEmail("john@example.com");
        submitRequest.setPayload("Test payload");

        dataRequest = new DataRequest();
        dataRequest.setId(1L);
        dataRequest.setName("John Doe");
        dataRequest.setEmail("john@example.com");
        dataRequest.setPayload("Test payload");
        dataRequest.setStatus(DataRequest.RequestStatus.PENDING);
    }

    @Test
    public void testSubmitDataRequest() {
        // Arrange
        when(dataRequestRepository.save(any(DataRequest.class))).thenReturn(dataRequest);

        // Act
        DataRequest result = dataProcessingService.submitDataRequest(submitRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(dataRequestRepository, times(1)).save(any(DataRequest.class));
    }

    @Test
    public void testGetDataById() {
        // Arrange
        when(dataRequestRepository.findById(1L)).thenReturn(Optional.of(dataRequest));

        // Act
        DataRequest result = dataProcessingService.getDataById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testGetDataByIdNotFound() {
        // Arrange
        when(dataRequestRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        DataRequest result = dataProcessingService.getDataById(999L);

        // Assert
        assertNull(result);
    }
}