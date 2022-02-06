package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestPlan;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.repository.TestPlanOriginRepository;

public class TestAllPlanService {

    public void testPlanProcess(TestPlan testPlan){
        TestPlanOrigin checkHintData = TestPlanOriginRepository.findSameRow(testPlan.getReqNo(), testPlan.getPlnDtm(), testPlan.getEngineerOne(), testPlan.getEngineerTwo(),
                testPlan.getVhclCode(),testPlan.getSpecSize(),testPlan.getBarcdNo(), testPlan.getSetSize(), testPlan.getTireFlow(), testPlan.getTestItemName()
                , testPlan.getRimSize(), testPlan.getAirPrss(), testPlan.getPgsStatus(),testPlan.getUdtDtm()).orElseGet(TestPlanOrigin::new);


    }

}
