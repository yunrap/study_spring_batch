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
    @Query("select h from TestPlan h where h.reqNo = :reqNo and h.plnDtm = :plnDtm and h.engineerOne = :engineerOne and (:engineerTwoNo is null or h.engineerTwo = :engineerTwo) and (:vhclCode is null or h.vhclCode = :vhclCode)" +
            "and (:specSize is null or h.specSize = :specSize) and (:barcodeNo is null or h.barcdNo = :barcdNo) and (:setSize is null or h.setSize = :setSize) and h.tireFlow = :tireFlow and h.testItemName = :testItemName and (:rimSize is null or h.rimSize = :rimSize) and " +
            "(:airPrss is null or h.airPrss = :airPrss) and h.pgsStatus = :pgsStatus and (:udtDtm is null or h.udtDtm = :udtDtm)")
    Optional<TestPlan> originfindSameRow
            (@Param("reqNo") String reqNo,
             @Param("plnDtm") LocalDateTime plnDtm,
             @Param("engineerOneNo") String engineerOneNo,
             @Param("engineerTwoNo")String engineerTwoNo,
             @Param("vhclCode")String vhclCode,
             @Param("specSize")String specSize,
             @Param("barcodeNo")String barcdNo,
             @Param("setSize")Integer setSize,
             @Param("tireFlow")String tireFlow,
             @Param("testItemName")String testItemName,
             @Param("rimSize")String rimSize,
             @Param("airPress")String airPrss,
             @Param("pgsStatus")String pgsStatus,
             @Param("udtDtm") LocalDateTime udtDtm);
}
