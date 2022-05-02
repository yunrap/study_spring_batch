package com.example.study_spring_batch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VehiclePlanType {

    TRAINING ("Training", "TYP03"), //연습
    MAINTENANCE ("Maintenance", "TYP04"), //점검, 유지보수
    DRIVE_EVENT("Drive Event", "TYP05"), //시승
    BUSINESS_TRIP("Business Trip", "TYP06"), //사내방문
    TEST("Test" , "TYP07"), //테스트
    ETC("Etc.", "TYP99"); //기타


    private String type;
    private String typeCode;
}
