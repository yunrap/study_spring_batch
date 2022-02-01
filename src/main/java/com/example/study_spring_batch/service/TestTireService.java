package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.repository.TestTireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TestTireService {

    private final TestTireRepository testTireRepository;

    public void insertTire(String planDay, TestPlanOrigin item){
        String barcodeNo = item.getBarcodeNo();
        String[] barcodeList = barcodeNo.split(", ");
        StringBuilder sb = new StringBuilder();


    }
}
