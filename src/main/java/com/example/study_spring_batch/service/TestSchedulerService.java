package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestSchedule;
import com.example.study_spring_batch.repository.TestScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestSchedulerService {

    private final TestScheduleRepository testScheduleRepository;

    public Optional<TestSchedule> findAllByTcReqNumAndTcDay(String regNo, String tcDay)
    {
        return testScheduleRepository.findByTcReqNumAndTcDay(regNo, tcDay);
    }

}
