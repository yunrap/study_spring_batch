package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;

@Builder     //디자인 패턴중하나인 빌더패턴 , 객체의 생성방법과 표현방법을 분리
@Setter
@Getter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TEST_PACKAGE_MAPPING")
public class TestPackageMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAP_SEQ")
    private int id;
    @Column(name = "TP_ID")
    private String tpId;
    @Column(name = "T_ID")
    private String tId;
    @Column(name = "TP_NAME")
    private String name;
    @Column(name = "T_NAME")
    private String tName;
    @Column(name = "T_LEVEL")
    private String tLevel;
    @Column(name = "T_PRICE")
    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_ID", insertable = false, updatable = false)
    private TestTrack testTrack;

}
