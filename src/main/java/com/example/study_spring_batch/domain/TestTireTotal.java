package com.example.study_spring_batch.domain;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@ToString
@Builder
@AllArgsConstructor
@Table(name = "TEST_TIRE_TOTAL")
public class TestTireTotal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOTAL_SEQ")
    private Integer totalSeq;
    @Column(name = "REQ_NO")
    private String reqNo;
    @Column(name = "PLN_DTM")
    private String plnDtm;
    @Column(name = "TEST_ITEM_NAME")
    private String testItemName;
    @Column(name = "SET_SIZE")
    private String setSize;
    @Column(name = "TIRE_SIZE")
    private String tireSize;
    @Column(name = "WHEEL_SIZE")
    private String wheelSize;
    @Column(name = "VHCL_CODE")
    private String vhclCode;
    @Column(name = "VHCL_NAME")
    private String vhclName;
    @Column(name = "RETURN_SCRAP")
    private String returnScrap;

    public TestTireTotal() {
        this.reqNo = "NONE";
    }
}
