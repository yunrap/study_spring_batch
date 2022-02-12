package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@Entity //객체와 테이블매핑
@ToString
@AllArgsConstructor
@Table(name = "TEST_SCHDULE") //객체와 테이블매핑
public class TestSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //TC_SEQ를 AUTO_INCREMENT 사용
    @Column(name = "TC_SEQ")
    private Integer tcSeq;
    @Column(name = "TC_DAY")
    private String tcDay;
    @Column(name = "TC_REQUEST_NUMBER")
    private String tcReqNum;
    @Column(name = "TC_DAY2")
    private String tcDayEnd;
    @Column(name = "COMP_CODE")
    private String compCode;
    @Column(name = "COMP_NAME")
    private String compName;
    @Column(name = "CAR_VENDER")
    private String carVender;
    @Column(name = "CAR_NAME")
    private String carName;
    @Column(name ="CAR_NUMBER")
    private String carNumber;
    @Column(name = "CAR_COLOR")
    private String carColor;
    @Column(name = "TC_PURPOSE")
    private String tcPurpose;
    @Column(name = "TC_AGREEMENT")
    private String tcAgreement;
    @Column(name = "TC_APPROVAL")
    private String tcApproval;
    @Column(name = "TC_STEP")
    private String tcStep;
    @Column(name = "TC_MEMO")
    private String tcMemo;
    @Column(name = "TC_REG_USER")
    private String tcRegUser;
    @Column(name = "TC_RESERV_CODE")
    private String tcReservCode;
    @Column(name = "TC_REG_DT")
    private String tcRegDate;
    @Column(name = "TC_MOD_USER")
    private String tcModUser;
    @Column(name = "TC_MOD_DT")
    private String tcModDate;

    public TestSchedule() {
        this.tcReqNum = "NONE";
    }
}
