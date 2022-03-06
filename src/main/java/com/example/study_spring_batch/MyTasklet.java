package com.example.study_spring_batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableCaching
@Configuration
public class MyTasklet implements Tasklet {


    private CacheManager cacheManager;

    public MyTasklet(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String name = (String) cacheManager.getCache("referenceData").get("foo").get();
        System.out.println("Hello " + name);
        return RepeatStatus.FINISHED;
    }
}
