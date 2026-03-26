package com.asyncpipeline.spring_async_data_pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
public class SpringAsyncDataPipelineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAsyncDataPipelineApplication.class, args);
    }
}