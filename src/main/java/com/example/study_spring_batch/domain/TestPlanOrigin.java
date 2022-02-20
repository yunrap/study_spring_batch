package com.example.study_spring_batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "TEST_PLAN_ORIGIN")
public class TestPlanOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HINT_NO")
    private int hintNo;

    @Column(name = "INSERT_FLAG")
    private String insertFlag;
    @Column(name = "UPDATE_FLAG")
    private String updateFlag;
    @Column(name = "REQ_NO")
    private String reqNo;
    @Column(name = "BARCD_NO")
    private String barcodeNo;
    @Column(name = "TEST_ITEM_NAME")
    private String testItemName;
    @Column(name = "PLN_DTM")
    private LocalDateTime plnDtm;
    @Column(name = "EGNR_EPNO_1")
    private String engineerOneNo;
    @Column(name = "EGNR_EPNO_2")
    private String engineerTwoNo;
    @Column(name = "VHCL_CODE")
    private String vhclCode;
    @Column(name = "SPEC_SIZE")
    private String specSize;
    @Column(name = "SET_SIZE")
    private int setSize;
    @Column(name = "TIRE_FLOW")
    private String tireFlow;
    @Column(name = "RIM_SIZE")
    private String rimSize;
    @Column(name = "AIR_PRSS")
    private String airPress;
    @Column(name = "PGS_STATUS")
    private String pgsStatus;
    @Column(name = "UDT_DTM")
    private LocalDateTime udtDtm;
    @Column(name = "TC_SEQ")
    private Integer tcSeq;
    @Column(name = "TR_SEQ")
    private Integer trSeq;
    @Column(name = "RM_SEQ_1")
    private Integer rmSeqOne;
    @Column(name = "RM_SEQ_2")
    private Integer rmSeqTwo;
    @Column(name = "TIRE_SEQ")
    private String tireSeq;
    @Column(name = "TM_SEQ")
    private Integer tmSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "VHCL_CODE", insertable = false, updatable = false)
    private TestBaminCar testCar;

    public TestPlanOrigin() { this.reqNo = "NONE";}
}
