package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestResourceMappingRepository  extends JpaRepository<TestResourceMapping,Long> {

    int deleteByTcSeq(int tcSeq);
}
