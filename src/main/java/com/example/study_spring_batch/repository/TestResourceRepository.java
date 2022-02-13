package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestResourceRepository extends JpaRepository<TestResource, Long> {
       //int countByTcSeqAndTcDayAndVhclCode(Integer tcSeq, String planDay, String vhclCode);

        Optional<TestResource> findByEmployeeNo(String employeeNo);

}
