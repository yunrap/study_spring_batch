
package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestPlan;
import com.example.study_spring_batch.domain.TestPlanBack;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.domain.TestSchedule;
import com.example.study_spring_batch.repository.IfTestPlanBackRepository;
import com.example.study_spring_batch.repository.IfTestPlnRepository;
import com.example.study_spring_batch.repository.TestPlanOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TestAllPlanService {

    private final TestPlanOriginRepository testPlanOriginRepository;
    private final IfTestPlnRepository ifTestPlnRepository;
    private final IfTestPlanBackRepository ifTestPlanBackRepository;

    private final TestSchedulerService testSchedulerService;
    private final TestTrackRsvService testTrackRsvService;
    private final TestResourceMappingService testResourceMappingService;

    private static final int ZERO = 0;
    private static final String NONE = "NONE";  //임의값 변수잡아줄때 사용
    private static final String CANCEL = "Cancel";
    private static final String DELETE = "Delete";
    private static Set<String> set = new HashSet<>();
    private static Set<String> ifTestPlanBackData = new HashSet<>();

    public void testPlanProcess(TestPlan testPlan, Map<Integer, String> ifTestPlanBack){

        if(ifTestPlanBackData == null || ifTestPlanBackData.isEmpty()){

            Iterator<Integer> keys = ifTestPlanBack.keySet().iterator();

            while (keys.hasNext()){
                Integer keyReqSeq = keys.next();
                TestPlan checkReqSeqdata = ifTestPlnRepository.findByReqSeq(keyReqSeq).orElseGet(TestPlan::new);

                if(checkReqSeqdata.getReqSeq() == ZERO) {      //IF_TEST_PLN_V 에서만 존재하는값일때 (삭제해야되는값)

                    ifTestPlanBackRepository.deleteByReqSeq(keyReqSeq); //1. BACKUP DB 삭제

                    String ifTestBackReqNo = ifTestPlanBack.get(keyReqSeq);
                    List<TestSchedule> testScheduleList = testSchedulerService.findAllByRegNo(ifTestBackReqNo);
                    testScheduleList.forEach(testSchedule -> deleteTestReservation(testSchedule.getTcSeq()));   //2. 추후 연관 테이블삭제
                     testPlanOriginRepository.deleteByReqNo(ifTestBackReqNo);

                    ifTestPlanBackData.add(ifTestPlanBack.get(keyReqSeq));
                }
            }
        }

        TestPlanOrigin checkPlanOriginData = testPlanOriginRepository.findSameRow(testPlan.getReqNo(), testPlan.getPlnDtm(), testPlan.getEngineerOne(), testPlan.getEngineerTwo(),
                testPlan.getVhclCode(),testPlan.getSpecSize(),testPlan.getBarcdNo(), testPlan.getSetSize(), testPlan.getTireFlow(), testPlan.getTestItemName()
                , testPlan.getRimSize(), testPlan.getAirPrss(), testPlan.getPgsStatus(),testPlan.getUdtDtm()).orElseGet(TestPlanOrigin::new);


        if(testPlan.getPgsStatus().equals(DELETE) || testPlan.getPgsStatus().equals(CANCEL)){
            List<TestSchedule> testScheduleList = testSchedulerService.findAllByRegNo(testPlan.getReqNo());

            testScheduleList.forEach(testSchedule ->
                    deleteTestReservation(testSchedule.getTcSeq()));
            testPlanOriginRepository.deleteByReqNo(testPlan.getReqNo());
            set.add(testPlan.getReqNo());

        }else{

            if(testPlan.getUdtDtm() != null){       //1. update가 존재할시
                if(checkPlanOriginData.getReqNo().equals(NONE)) {   //IF_TEST_PLN , TEST_PLAN_ORIGIN 일치 x && 변경된 값

                    if(!set.contains(testPlan.getReqNo()))  //update시 삭제한다. (delete, cancle제외)
                    {
                        List<TestSchedule> testScheduleList = testSchedulerService.findAllByRegNo(testPlan.getReqNo());

                        testScheduleList.forEach(testSchedule ->
                                deleteTestReservation(testSchedule.getTcSeq()));
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

            }else{   //2. update가 존재안할시

                if(checkPlanOriginData.getReqNo().equals(NONE)) {   //IF_TEST_PLN , TEST_PLAN_ORIGIN 일치 x && 새로운 값
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

    void deleteTestReservation(int tcSeq){
        testResourceMappingService.deleteTestResourceMapping(tcSeq);
        testSchedulerService.deleteTestSchedule(tcSeq);
        testTrackRsvService.deleteTrackReservation(tcSeq);
    }

}

