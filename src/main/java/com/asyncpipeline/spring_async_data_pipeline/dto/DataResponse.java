package com.asyncpipeline.spring_async_data_pipeline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.asyncpipeline.spring_async_data_pipeline.entity.DataRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse {

    private Long id;
    private String name;
    private String email;
    private String payload;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public static DataResponse fromEntity(DataRequest request) {
        DataResponse response = new DataResponse();
        response.setId(request.getId());
        response.setName(request.getName());
        response.setEmail(request.getEmail());
        response.setPayload(request.getPayload());
        response.setStatus(request.getStatus().toString());
        response.setCreatedAt(request.getCreatedAt());
        response.setProcessedAt(request.getProcessedAt());
        return response;
    }
}