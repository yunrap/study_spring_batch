package com.example.study_spring_batch.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TestPlan {
    private String reqNo;
    private LocalDateTime plnDtm;
    private String engineerOne;
    private String engineerTwo;
    private String vhclCode;
    private String specSize;
    private String barcdNo;
    private int setSize;
    private String tireFlow;
    private String testItemName;
    private String rimSize;
    private String airPrss;
    private String pgsStatus;
    private LocalDateTime udtDtm;
}
