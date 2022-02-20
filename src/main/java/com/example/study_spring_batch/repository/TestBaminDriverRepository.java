package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestBaminDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestBaminDriverRepository  extends JpaRepository<TestBaminDriver, String> {

}
