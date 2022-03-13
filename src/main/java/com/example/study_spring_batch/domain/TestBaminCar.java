package com.example.study_spring_batch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "TEST_BAMIN_CAR")
public class TestBaminCar {
    @Id
    @Column(name = "VHCL_CODE")
    private int vhclCode;
    @Column(name = "VHCL_RGSNO")
    private String rgsNo;
    @Column(name = "VHCL_MAKER")
    private String maker;
    @Column(name = "VHCL_NAME")
    private String name;
    @Column(name = "VHCL_CLR")
    private String color;
    @Column(name = "MNG_FG")
    private String mngFg;
    @Column(name = "CRN_DTM")
    private LocalDateTime createTime;
    @Column(name = "UDT_DTM")
    private LocalDateTime updateTime;

}
