package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.domain.TestSchedule;
import com.example.study_spring_batch.repository.TestScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestSchedulerService {

    private final TestScheduleRepository testScheduleRepository;
    private static final String tcApproval = "3";
    private static final String tcStep = "00000";
    private static final String compCode = "BBAMIN";
    private static final String YES = "Y";
    private static final String BAMINDV = "배달의민족";
    private static final String BAMIN = "BAMIN";

    public Optional<TestSchedule> findAllByRegNoAndTcDay(String regNo, String tcDay)
    {
        return testScheduleRepository.findByRegNoAndTcDay(regNo, tcDay);
    }

    public List<TestSchedule> findAllByRegNo(String regNo) {return testScheduleRepository.findByRegNo(regNo);}

    public void insertTestSchedule(String planDay, String tcReservationCode, TestPlanOrigin tpo){
        TestSchedule testSchedule = TestSchedule.builder()
                .tcDay(planDay)
                .tcDayEnd(planDay)
                .regNo(tpo.getReqNo())
                .tcReservCode(tcReservationCode)
                .carNumber(tpo.getVhclCode() == null ? "" : tpo.getTestCar().getRgsNo())
                .carVender(tpo.getVhclCode() == null ? "" : tpo.getTestCar().getMaker())
                .carName(tpo.getVhclCode() == null ? "" : tpo.getTestCar().getName())
                .carColor(tpo.getVhclCode() == null ? "" : tpo.getTestCar().getColor())
                .tcRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .tcAgreement(YES)
                .tcPurpose("BAMIN 시험")
                .tcRegUser(BAMIN)
                .compName(BAMINDV)
                .tcApproval(tcApproval)
                .tcStep(tcStep)
                .compCode(compCode)
                .build();
        testScheduleRepository.save(testSchedule);

        tpo.setTcSeq(testSchedule.getTcSeq());
    }

    @Transactional
    public void deleteTestSchedule(int tcSeq) {
        testScheduleRepository.deleteByTcSeq(tcSeq);
    }

}
