package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestDriver;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.domain.TestResource;
import com.example.study_spring_batch.domain.TestResourceMapping;
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
    private static final String ONE = "ONE";
    private static final String TWO = "TWO";
    private static final String NONE = "0";

    public void insertEngineer(String planDay, TestResource testResource, TestDriver testDriver, TestPlanOrigin tpo, String type){
        TestResourceMapping testResourceMapping = null;

        //int dupCount = testResourceRepository.countByTcSeqAndTcDayAndVhclCode(tpo.getTcSeq(), planDay, tpo.getVhclCode());

        if(testResource.getEmployeeNo().equals("0")){      //TEST_RESOURCE employeeno가 없을땐
            testResourceMapping = TestResourceMapping.builder()
                    .tcSeq(tpo.getTcSeq())
                    .tcDay(planDay)
                    .driverNumber(Integer.parseInt(testDriver.getEmployeeNo()))
                    .driverName(testDriver.getName())
                    .level(testDriver.getEngineerLevel())
                    .inOut("I")
                    .dccpYn("N")
                    .registerDay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                    .build();

            if (testResourceMapping != null && type.equals(ONE)) {
                tpo.setRmSeqOne(testResourceMapping.getRmSeq());
            }
            if (testResourceMapping != null && type.equals(TWO)) {
                tpo.setRmSeqTwo(testResourceMapping.getRmSeq());
            }
        }
    }
}
