package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="IF_TEST_PLN")
public class TestPlan {
    @Id
    @Column(name = "reqNo", nullable = false)
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
