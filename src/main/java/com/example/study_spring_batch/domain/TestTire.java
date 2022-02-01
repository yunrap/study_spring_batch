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
@Table(name = "PG_TIRE")
public class TestTire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIRE_SEQ")
    private Integer tireSeq;
    @Column(name = "TC_SEQ")
    private int tcSeq;
    @Column(name = "TC_DAY")
    private String tcDay;
    @Column(name = "TEST_ITEM_NAME")
    private String testItemName;
    @Column(name = "REQ_NO")
    private String reqNo;
    @Column(name = "TIRE_BARCODE_NO")
    private String barcodeNo;
}
