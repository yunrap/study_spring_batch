package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestPlanBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IfTestPlanBackRepository extends JpaRepository<TestPlanBack, Integer> {

    Optional<TestPlanBack> findByReqSeq(int reqSeq);

    int deleteByReqSeq(int reqSeq);

}
