package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TEST_TRACK")
public class TestTrack {
    @Id
    @Column(name = "T_ID")
    private String tId;
    @Column(name = "T_NAME")
    private String tName;
    @Column(name = "T_MAX")
    private int tMax;
    @Column(name = "T_LEVEL")
    private String tLevel;
    @Column(name = "T_PRICE")
    private int tPrice;
    @Column(name = "T_NICKNAME")
    private String nickName;
}
