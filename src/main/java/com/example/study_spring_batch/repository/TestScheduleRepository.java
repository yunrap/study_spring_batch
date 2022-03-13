package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestScheduleRepository extends JpaRepository<TestSchedule,Long> {

    Optional<TestSchedule> findByRegNoAndTcDay(String regNo, String tcDay);

    List<TestSchedule> findByRegNo(String regNo);

    int deleteByTcSeq(int tcSeq);
}
