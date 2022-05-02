package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.VehiclePlan;
import com.example.study_spring_batch.domain.VehiclePlanOrigin;
import com.example.study_spring_batch.domain.VehiclePlanType;
import com.example.study_spring_batch.repository.VehiclePlanOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehiclePlanService {


    private final VehiclePlanOriginRepository vehiclePlanOriginRepository;

    private static final String DELETE = "Y";    //<수정>
    private static final String TRACKLIST = "BRAKING,DHC,GNR,HSO,NVH,PBN,RIDE,VDA,WHC";    //<수정>

    private static Set<String> set = new HashSet<>();

    public void vehiclePlanProcess(VehiclePlan vehiclePlan) {
        System.out.println();
        VehiclePlanOrigin checkData = vehiclePlanOriginRepository.findSameRow(vehiclePlan).orElseGet(VehiclePlanOrigin::new);


        if(checkData.getPlnNo() ==0){
            System.out.println(vehiclePlan.getVhclCode() + "변경된값");
            saveEntity(vehiclePlan);
        }
    }

    private void saveEntity(VehiclePlan v) {
        vehiclePlanOriginRepository.save(VehiclePlanOrigin.builder()
                .vhclCode(v.getVhclCode())
                .insertFlag("Y")
                .plnDtmStart(v.getPlnDtmStart())
                .plnDtmEnd(v.getPlnDtmEnd())
                .engineer(v.getEngineer())
                .useObj(v.getUseObj())
                .testRoad(TRACKLIST)
                .deleteYn(v.getDeleteYn())
                .crnDtm(v.getCrnDtm())
                .udtDtm(v.getUdtDtm())
                .build());
    }

    public static String getTcApprovalCode(String useObj) {
        if(useObj.equals(VehiclePlanType.TEST.getType())) {
            return VehiclePlanType.TEST.getTypeCode();
        }
        if(useObj.equals(VehiclePlanType.TRAINING.getType())) {
            return VehiclePlanType.TRAINING.getTypeCode();
        }
        if(useObj.equals(VehiclePlanType.BUSINESS_TRIP.getType())) {
            return VehiclePlanType.BUSINESS_TRIP.getTypeCode();
        }
        if(useObj.equals(VehiclePlanType.DRIVE_EVENT.getType())) {
            return VehiclePlanType.DRIVE_EVENT.getTypeCode();
        }
        if(useObj.equals(VehiclePlanType.MAINTENANCE.getType())) {
            return VehiclePlanType.MAINTENANCE.getTypeCode();
        }
        if(useObj.equals(VehiclePlanType.ETC.getType())) {
            return VehiclePlanType.ETC.getTypeCode();
        }

        return "";
    }

}
