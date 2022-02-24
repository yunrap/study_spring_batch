package com.example.study_spring_batch.service;

import com.example.study_spring_batch.domain.TestPackageMapping;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.domain.TestTrackReservation;
import com.example.study_spring_batch.repository.TestTrackRsrvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TestTrackRsvService {

    private final TestTrackRsrvRepository testTrackRsrvRepository;
    private static final String trackType = "TYP00";
    private static final String startHour = "0000";
    private static final String endHour = "2359";

    @Transactional
    public void insertTrackReservation(String planDay, TestPackageMapping testPackageMapping, TestPlanOrigin tpo){
        TestTrackReservation testTrackReservation = TestTrackReservation.builder()
                .tcSeq(tpo.getTcSeq())
                .tcDay(planDay)
                .requestNumber(tpo.getReqNo())
                .trackType(trackType)
                .trackName(testPackageMapping.getTName())
                .trackCode(testPackageMapping.getTId())
                .trackLevel(testPackageMapping.getTLevel())
                .trackPrice(testPackageMapping.getPrice())
                .trackNickName(testPackageMapping.getTestTrack().getNickName())
                .packageCode(testPackageMapping.getTpId())
                .packageName(testPackageMapping.getName())
                .startHour(startHour)
                .endHour(endHour)
                .build();
        testTrackRsrvRepository.save(testTrackReservation);
        tpo.setTrSeq(testTrackReservation.getTrSeq());

    }
}
