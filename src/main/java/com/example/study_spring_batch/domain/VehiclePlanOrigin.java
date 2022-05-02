package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@ToString
@Table(name = "VEHICLE_PLAN_ORIGIN")
public class VehiclePlanOrigin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLN_NO")
    private Integer plnNo;
    @Column(name = "INSERT_FLAG")
    private String insertFlag;

    @Column(name = "VHCL_CODE")
    private String vhclCode;
    @Column(name = "PLN_DTM_S")
    private LocalDateTime plnDtmStart;
    @Column(name = "PLN_DTM_E")
    private LocalDateTime plnDtmEnd;
    @Column(name = "EGNR_EPNO")
    private String engineer;
    @Column(name = "USE_OBJ")
    private String useObj;
    @Column(name = "TEST_ROAD")
    private String testRoad;
    @Column(name = "DELETE_YN")
    private String deleteYn;
    @Column(name = "CRN_DTM")
    private LocalDateTime crnDtm;
    @Column(name = "UDT_DTM")
    private LocalDateTime udtDtm;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VHCL_CODE", insertable = false, updatable = false)
    private TestBaminCar baminCar;

    public VehiclePlanOrigin() {
        this.plnNo = 0;
    }
}