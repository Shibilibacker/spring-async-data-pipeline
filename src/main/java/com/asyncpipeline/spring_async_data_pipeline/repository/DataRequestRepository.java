package com.asyncpipeline.spring_async_data_pipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.asyncpipeline.spring_async_data_pipeline.entity.DataRequest;

import java.util.List;

@Repository
public interface DataRequestRepository extends JpaRepository<DataRequest, Long> {
    List<DataRequest> findByStatus(DataRequest.RequestStatus status);
}