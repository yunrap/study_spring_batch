package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestResource;
import com.example.study_spring_batch.domain.TestResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestResourceRepository extends JpaRepository<TestResourceMapping, Long> {
       int countByTcSeqAndTcDayAndDriverNumber(Integer tcSeq, String planDay, String driverNumber);


}
