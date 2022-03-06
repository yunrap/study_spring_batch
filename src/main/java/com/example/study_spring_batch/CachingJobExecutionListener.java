package com.example.study_spring_batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.cache.CacheManager;

public class CachingJobExecutionListener implements JobExecutionListener {
//    @Override
//    public void beforeJob(JobExecution jobExecution) {
//        System.out.println("Job is Started");
//        System.out.println("jOB IS ");
//    }
//
//    @Override
//    public void afterJob(JobExecution jobExecution) {
//        long startTime = jobExecution.getStartTime().getTime();
//        long endTime = jobExecution.getEndTime().getTime();
//
//        System.out.println("총 소요시간 : " + (endTime - startTime));
//    }

    private CacheManager cacheManager;

    public CachingJobExecutionListener(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        // populate cache as needed. Can use a jdbcTemplate to query the db here and populate the cache
        cacheManager.getCache("referenceData").put("foo", "bar");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // clear cache when the job is finished
        cacheManager.getCache("referenceData").clear();
    }

}
