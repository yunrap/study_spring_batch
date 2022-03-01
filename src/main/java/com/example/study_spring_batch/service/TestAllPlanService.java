
package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestPlan;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.repository.IfTestPlnRepository;
import com.example.study_spring_batch.repository.TestPlanOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TestAllPlanService {

    private final IfTestPlnRepository ifTestPlnRepository;
    private final TestPlanOriginRepository testPlanOriginRepository;

    private static final String NONE = "NONE";  //임의값 변수잡아줄때 사용
    private static Set<String> set = new HashSet<>();
    private final Map<String, TestPlan> testPlanList = new HashMap<>();

    public void testPlanProcess(TestPlan testPlan){

        TestPlan checkOriginData = ifTestPlnRepository.originfindSameRow(testPlan.getReqNo(), testPlan.getPlnDtm(), testPlan.getEngineerOne(), testPlan.getEngineerTwo(),
                testPlan.getVhclCode(),testPlan.getSpecSize(),testPlan.getBarcdNo(), testPlan.getSetSize(), testPlan.getTireFlow(), testPlan.getTestItemName()
                , testPlan.getRimSize(), testPlan.getAirPrss(), testPlan.getPgsStatus(),testPlan.getUdtDtm()).orElseGet(TestPlan::new);
        System.out.println("======checkHintData" + checkOriginData.getReqNo());   //만약 비워있지않으면



        TestPlanOrigin checkPlanOriginData = testPlanOriginRepository.findSameRow(testPlan.getReqNo(), testPlan.getPlnDtm(), testPlan.getEngineerOne(), testPlan.getEngineerTwo(),
                testPlan.getVhclCode(),testPlan.getSpecSize(),testPlan.getBarcdNo(), testPlan.getSetSize(), testPlan.getTireFlow(), testPlan.getTestItemName()
                , testPlan.getRimSize(), testPlan.getAirPrss(), testPlan.getPgsStatus(),testPlan.getUdtDtm()).orElseGet(TestPlanOrigin::new);
        System.out.println("======checkHintData" + checkPlanOriginData.getReqNo());   // 변경된값 or 새로운값 일때 NONE출력


        //1. update가 존재할시
        if(testPlan.getUdtDtm() != null){
            if(checkPlanOriginData.getReqNo().equals(NONE)) {   //IF_TEST_PLN , TEST_PLAN_ORIGIN 일치 x && 변경된 값

                if(!set.contains(testPlan.getReqNo()))  //같은 ReqNo단위 체크
                {
                    testPlanOriginRepository.deleteByReqNo(testPlan.getReqNo());
                    set.add(testPlan.getReqNo());
                }


                TestPlanOrigin result = TestPlanOrigin.builder()
                        .insertFlag("Y")
                        .updateFlag("Y")
                        .reqNo(testPlan.getReqNo()).plnDtm(testPlan.getPlnDtm())
                        .engineerOneNo(testPlan.getEngineerOne()).engineerTwoNo(testPlan.getEngineerTwo())
                        .vhclCode(testPlan.getVhclCode()).specSize(testPlan.getSpecSize()).barcodeNo(testPlan.getBarcdNo())
                        .setSize(testPlan.getSetSize()).tireFlow(testPlan.getTireFlow()).testItemName(testPlan.getTestItemName())
                        .rimSize(testPlan.getRimSize()).airPress(testPlan.getAirPrss()).pgsStatus(testPlan.getPgsStatus())
                        .udtDtm(testPlan.getUdtDtm())
                        .build();

                testPlanOriginRepository.save(result);
            }

        }else{
            //2. update가 존재안할시
            if(checkPlanOriginData.getReqNo().equals(NONE)) {   //IF_TEST_PLN , TEST_PLAN_ORIGIN 일치 x && 새로운 값

                //step2 : 만약, IF_TEST_PLN 값이 수정된다면

                //step1 : 새로운데이터를, TEST_PLAN_ORIGIN에 데이터를 넣어준다.
                testPlanOriginRepository.save(TestPlanOrigin.builder()
                        .insertFlag("Y")
                        .updateFlag("N")
                        .reqNo(testPlan.getReqNo()).plnDtm(testPlan.getPlnDtm())
                        .engineerOneNo(testPlan.getEngineerOne()).engineerTwoNo(testPlan.getEngineerTwo())
                        .vhclCode(testPlan.getVhclCode()).specSize(testPlan.getSpecSize()).barcodeNo(testPlan.getBarcdNo())
                        .setSize(testPlan.getSetSize()).tireFlow(testPlan.getTireFlow()).testItemName(testPlan.getTestItemName())
                        .rimSize(testPlan.getRimSize()).airPress(testPlan.getAirPrss()).pgsStatus(testPlan.getPgsStatus())
                        .udtDtm(testPlan.getUdtDtm())
                        .build());
            }
        }

    }

}

