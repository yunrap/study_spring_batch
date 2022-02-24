package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestPackageMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestPackageMappingRepository extends JpaRepository<TestPackageMapping, Long> {
    @Query("select p from TestPackageMapping p join fetch p.testTrack t where p.name=:name")
    List<TestPackageMapping> findAllByName(String name);
}
