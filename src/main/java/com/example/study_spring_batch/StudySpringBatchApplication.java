package com.example.study_spring_batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableBatchProcessing  // 스프링배치 작동 어노테이션
@SpringBootApplication
public class StudySpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringBatchApplication.class, args);
    }

}
