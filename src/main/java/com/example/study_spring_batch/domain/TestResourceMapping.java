package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@ToString
@Table(name = "TEST_RESOURCE_MAPPING")
public class TestResourceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RM_SEQ")
    private int rmSeq;
    @Column(name ="D_SEQ")
    private int driverNumber;
    @Column(name = "RM_LEVEL") //운전자 or 차량에 따라 매핑될 레벨
    private String level;
    @Column(name = "RM_TYPE") //운전자 (D) 차량 (C)
    private String rfidType;
    @Column(name = "D_NAME")
    private String driverName;
    @Column(name = "TC_SEQ")
    private int tcSeq;
    @Column(name = "TC_DAY")
    private String tcDay;
    @Column(name = "R_ID")
    private String rId;
    @Column(name = "W_ID")
    private String wId;
    @Column(name = "C_CODE")
    private String vhclCode;
    @Column(name = "RM_R_YN")
    private String rfidYn;
    @Column(name = "RM_W_YN")
    private String wirelessYn;
    @Column(name = "N_DCCP_YN")
    private String dccpYn;
    @Column(name = "RM_IN_OUT")
    private String inOut;
    @Column(name = "RM_REG_DT")
    private String registerDay;

    public TestResourceMapping() {
        this.driverNumber = 0;
        this.vhclCode = "0";
    }
}
