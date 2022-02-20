package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestBaminResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestBaminResourceRepository extends JpaRepository<TestBaminResource, Long> {

    Optional<TestBaminResource> findByEmployeeNo(String employeeNo);

}
