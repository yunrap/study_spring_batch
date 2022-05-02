package com.example.study_spring_batch.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class VehiclePlan {

    private String vhclCode;
    private LocalDateTime plnDtmStart;
    private LocalDateTime plnDtmEnd;
    private String engineer;
    private String useObj;
    private String testRoad;
    private String deleteYn;
    private LocalDateTime crnDtm;
    private LocalDateTime udtDtm;
}