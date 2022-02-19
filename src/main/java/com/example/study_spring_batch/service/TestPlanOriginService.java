package com.example.study_spring_batch.service;

import com.example.study_spring_batch.repository.TestPlanOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TestPlanOriginService {

    private final TestPlanOriginRepository testPlanOriginRepository;

    @Transactional
    public void deleteTestPlanOrigin(String reqNo) {
        testPlanOriginRepository.deleteByReqNo(reqNo);
    }


}
