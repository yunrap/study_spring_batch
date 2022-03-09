package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestPlan;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IfTestPlnRepository extends JpaRepository<TestPlan, Integer> {

    Optional<TestPlan> findByReqSeq(int reqSeq);

}
