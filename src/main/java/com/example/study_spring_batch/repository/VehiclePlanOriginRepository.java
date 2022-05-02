package com.example.study_spring_batch.repository;

import com.example.study_spring_batch.domain.VehiclePlan;
import com.example.study_spring_batch.domain.VehiclePlanOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiclePlanOriginRepository extends JpaRepository<VehiclePlanOrigin,Integer> {
//    @Query("SELECT v FROM VehiclePlanOrigin v JOIN fetch v.hintCar c")
//    List<VehiclePlanOrigin> findAll();


    @Query(value = "select v from VehiclePlanOrigin v " +
            "where v.vhclCode = :#{#vehicle.vhclCode} and v.plnDtmStart = :#{#vehicle.plnDtmStart} " +
            "and (v.plnDtmEnd is null or v.plnDtmEnd = :#{#vehicle.plnDtmEnd}) and v.engineer =:#{#vehicle.engineer} " +
            "and v.useObj = :#{#vehicle.useObj} and v.deleteYn =:#{#vehicle.deleteYn} " +
            "and v.crnDtm = :#{#vehicle.crnDtm} and (v.udtDtm is null or v.udtDtm =:#{#vehicle.udtDtm})")
    Optional<VehiclePlanOrigin> findSameRow(@Param("vehicle") VehiclePlan vehicle);

    @Transactional
    void deleteByVhclCodeAndUseObjAndPlnDtmStart(String vhclCode, String useObj, LocalDateTime plnDtmStart);     //<수정>


}
