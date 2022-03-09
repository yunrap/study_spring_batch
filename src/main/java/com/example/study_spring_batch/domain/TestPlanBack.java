package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "IF_TEST_PLN_V_BACK")
public class TestPlanBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQ_SEQ")
    private int reqSeq;

    @Column(name = "REQ_NO", insertable=false, updatable=false)
    private String reqNo;
    @Column(name = "PLN_DTM")
    private LocalDateTime plnDtm;
    @Column(name = "EGNR_EPNO_1")
    private String engineerOne;
    @Column(name = "EGNR_EPNO_2")
    private String engineerTwo;
    @Column(name = "VHCL_CODE")
    private String vhclCode;
    @Column(name = "SPEC_SIZE")
    private String specSize;
    @Column(name = "BARCD_NO")
    private String barcdNo;
    @Column(name = "SET_SIZE")
    private int setSize;
    @Column(name = "TIRE_FLOW")
    private String tireFlow;
    @Column(name = "TEST_ITEM_NAME")
    private String testItemName;
    @Column(name = "RIM_SIZE")
    private String rimSize;
    @Column(name = "AIR_PRSS")
    private String airPrss;
    @Column(name = "PGS_STATUS")
    private String pgsStatus;
    @Column(name = "UDT_DTM")
    private String udtDtm;

}
