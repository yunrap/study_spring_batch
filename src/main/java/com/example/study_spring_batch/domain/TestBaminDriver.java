package com.example.study_spring_batch.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@ToString
@Table(name = "TEST_BAMIN_DRIVER")
public class TestBaminDriver {

    @Id
    @Column(name = "EMPLOYEE_NO")
    private String employeeNo;
    @Column(name = "COMPANY_CODE")
    private String companyCode;
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EGNR_GR")
    private String engineerLevel;
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "DEPT_CODE")
    private String deptCode;
    @Column(name = "CRN_DTM")
    private LocalDateTime createTime;
    @Column(name = "UDT_DTM")
    private LocalDateTime updateTime;

    public TestBaminDriver() {
        this.employeeNo = "NONE";
    }

}
