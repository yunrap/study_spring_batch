package com.example.study_spring_batch.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TEST_TRACK_RESERV")
public class TestTrackReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TR_SEQ")
    private int trSeq;
    @Column(name = "TC_SEQ")
    private int tcSeq;
    @Column(name = "TC_DAY")
    private String tcDay;
    @Column(name = "TR_TRACK_CODE")
    private String trackCode;
    @Column(name ="TC_REQUEST_NUMBER")
    private String requestNumber;
    @Column(name = "TR_TRACK_TYPE")
    private String trackType;
    @Column(name = "TR_LEVEL")
    private String trackLevel;
    @Column(name = "TR_PRICE")
    private int trackPrice;
    @Column(name = "TR_TRACK_NAME")
    private String trackName;
    @Column(name = "TR_PACKAGE_NAME")
    private String packageName;
    @Column(name = "TR_PACKAGE_CODE")
    private String packageCode;
    @Column(name = "TR_RESERV_ST_HOUR")
    private String startHour;
    @Column(name = "TR_RESERV_ED_HOUR")
    private String endHour;
    @Column(name = "TR_TRACK_NICKNAME")
    private String trackNickName;
}
