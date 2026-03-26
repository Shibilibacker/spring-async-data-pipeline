package com.asyncpipeline.spring_async_data_pipeline.controller;

import com.asyncpipeline.spring_async_data_pipeline.dto.SubmitDataRequest;
import com.asyncpipeline.spring_async_data_pipeline.entity.DataRequest;
import com.asyncpipeline.spring_async_data_pipeline.service.DataProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DataProcessingService dataProcessingService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testSubmitData() throws Exception {
        // Arrange
        when(dataProcessingService.submitDataRequest(any(SubmitDataRequest.class)))
                .thenReturn(dataRequest);

        // Act & Assert
        mockMvc.perform(post("/v1/data/submit")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void testSubmitDataWithoutAuth() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/data/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetData() throws Exception {
        // Arrange
        when(dataProcessingService.getDataById(1L)).thenReturn(dataRequest);

        // Act & Assert
        mockMvc.perform(get("/v1/data/1")
                .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void testHealth() throws Exception {
        mockMvc.perform(get("/v1/data/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}