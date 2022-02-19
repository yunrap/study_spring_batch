package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@Entity
@ToString
@AllArgsConstructor
@Table(name = "TEST_BAMIN_RESOURCE")
public class TestResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HR_SEQ")
    private int hrSeq;
    @Column(name = "VHCL_CODE")
    private String vhclCode;
    @Column(name = "EMPLOYEE_NO")
    private String employeeNo;
    @Column(name = "HR_TYPE")
    private String hrType;
    @Column(name = "W_ID")
    private String wId;
    @Column(name = "D_NAME")
    private String dName;
    @Column(name = "R_ID")
    private String rId;

    public TestResource() {
        this.employeeNo = "0";
        this.vhclCode = "0";
    }

}
