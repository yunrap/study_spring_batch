package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.TestTrackReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTrackRsrvRepository extends JpaRepository<TestTrackReservation, Long> {
    int deleteByTcSeq(int tcSeq);
}
