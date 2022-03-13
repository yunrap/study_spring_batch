package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestBaminDriver;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.domain.TestBaminResource;
import com.example.study_spring_batch.domain.TestResourceMapping;
import com.example.study_spring_batch.repository.TestResourceMappingRepository;
import com.example.study_spring_batch.repository.TestResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TestResourceMappingService {

    private final TestResourceRepository testResourceRepository;
    private final TestResourceMappingRepository testResourceMappingRepository;

    private static final String ONE = "ONE";
    private static final String TWO = "TWO";
    private static final String CAR_LEVEL = "A";
    private static final String CAR = "C";
    private static final String NONE = "0";

    @Transactional
    public void insertVhclCode(String planDay, TestBaminResource testResource, TestPlanOrigin tpo)
    {
        TestResourceMapping testResourceMapping = null;

        int dupCount = testResourceRepository.countByTcSeqAndTcDayAndVhclCode(tpo.getTcSeq(), planDay, tpo.getVhclCode() == null ? "" : tpo.getVhclCode());

        if(dupCount == 0){
            if(testResource.getVhclCode().equals(NONE)){
                testResourceMapping = TestResourceMapping.builder()
                        .tcSeq(tpo.getTcSeq())
                        .tcDay(planDay)
                        .rfidType(CAR)
                        .vhclCode(tpo.getVhclCode() == null ? "" : tpo.getVhclCode())
                        .level(CAR_LEVEL)
                        .inOut("I")
                        .dccpYn("Y")
                        .registerDay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                        .build();
                testResourceRepository.save(testResourceMapping);
            }
        }
    }



    @Transactional
    public void insertEngineer(String planDay, TestBaminResource testResource, TestBaminDriver testDriver, TestPlanOrigin tpo, String type){
        TestResourceMapping testResourceMapping = null;

        int dupCount = testResourceRepository.countByTcSeqAndTcDayAndDriverNumber(tpo.getTcSeq(), planDay, 'H'+testDriver.getEmployeeNo());

        if(dupCount == 0) {
            if(testResource.getEmployeeNo().equals(NONE)){      //TEST_RESOURCE employeeno가 없을땐 (직접등록은 웹에서 구현)
                testResourceMapping = TestResourceMapping.builder()
                        .tcSeq(tpo.getTcSeq())
                        .tcDay(planDay)
                        .driverNumber('H' + testDriver.getEmployeeNo())
                        .driverName(testDriver.getName())
                        .level(testDriver.getEngineerLevel())
                        .inOut("I")
                        .dccpYn("N")
                        .registerDay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                        .build();

                testResourceRepository.save(testResourceMapping);

                if (testResourceMapping != null && type.equals(ONE)) {
                    tpo.setRmSeqOne(testResourceMapping.getRmSeq());
                }
                if (testResourceMapping != null && type.equals(TWO)) {
                    tpo.setRmSeqTwo(testResourceMapping.getRmSeq());
                }
            }
        }
    }

    @Transactional
    public void deleteTestResourceMapping(int tcSeq){
        testResourceMappingRepository.deleteByTcSeq(tcSeq);
    }
}
