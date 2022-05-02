package com.example.study_spring_batch;


import com.example.study_spring_batch.domain.*;
import com.example.study_spring_batch.repository.TestBaminDriverRepository;
import com.example.study_spring_batch.repository.TestBaminResourceRepository;
import com.example.study_spring_batch.repository.TestPackageMappingRepository;
import com.example.study_spring_batch.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories
public class TestJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory originEntityManagerFactory;  //DB 당 하나씩사용

    private final TestAllPlanService testAllPlanService;
    private final TestSchedulerService testSchedulerService;
    private final TestResourceMappingService testResourceMappingService;
    private final TestTrackRsvService testTrackRsvService;
    private final VehiclePlanService vehiclePlanService;
    private final DataSource dataSource;

    private final TestBaminDriverRepository testDriverRepository;
    private final TestBaminResourceRepository testBaminResourceRepository;
    private final TestPackageMappingRepository testPackageMappingRepository;

    private static final String NONE = "NONE";
    private final int chunckSize = 1;
    private static String tcReservationCode = "T220101H000";

    public Map<Integer, String> ifTestPlanBack = new HashMap<>();


    @Bean
    public Job testJob(){
        return jobBuilderFactory.get("testJob")
                .start(saveIfTestPlan(null)) // 시험 계획정보 BACK UP
                .next(findAllTestPlan(null))   //시험 계획정보 EAI 연동
                .next(findMaxResvNumber(null)) //테스트 스케쥴에 저장된 가장 최근 ReservationCode 조회
                .next(insertTestScheduleStep(null))// 조회한 시험 계획정보 INSERT
                //.next(totalTestTireData(null))  //타이어 집계
                .start(findVehiclePlanStep(null)) // 시험 외 정보 VIEW 조회
                .next(insertEtcScheduleStep(null)) // 시험 외 정보 INSERT
                .build();
    }

    @Bean
    @JobScope
    public Step saveIfTestPlan(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("saveIfTestPlan")
                .<TestPlanBack,TestPlanBack>chunk(100)
                .reader(saveIfTestReader())
                .writer(saveIfTestWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<TestPlanBack> saveIfTestReader(){
        return new JpaPagingItemReaderBuilder<TestPlanBack>()
                .name("saveIfTestReader")
                .entityManagerFactory(originEntityManagerFactory)
                .pageSize(100)
                .queryString("select m from TestPlanBack m order by m.reqSeq desc")
                .build();
    }


    @Bean
    public ItemWriter<TestPlanBack> saveIfTestWriter() {
        return list -> {
            for (TestPlanBack testPlanBack : list) {
                ifTestPlanBack.put(testPlanBack.getReqSeq(), testPlanBack.getReqNo());
                System.out.println(ifTestPlanBack.toString() + "00000");
            }
        };
    }


    @Bean
    @JobScope
    public Step findAllTestPlan(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("findAllTestPlan")
                .<TestPlan,TestPlan>chunk(chunckSize)//각 커밋 사이에 처리되는 row수 <reader 에서 input return, processing후 output return>
                .reader(findAllTestReader())
                .writer(findAllPlanWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<TestPlan> findAllTestReader() {
        return new JdbcCursorItemReaderBuilder<TestPlan>()
                .name("findAllPlanReader")  //itemReader의 이름
                .fetchSize(chunckSize)      //한번에 읽어올 Size
                .dataSource(dataSource)     //연결할 DB의 datasource
                .rowMapper(new BeanPropertyRowMapper<>(TestPlan.class))     //sql에서 가져온 data를 Object로 바꿔줄 Mapper
                .sql("SELECT REQ_NO, PLN_DTM, EGNR_EPNO_1 as engineerOne, EGNR_EPNO_2 as engineerTwo, VHCL_CODE, SPEC_SIZE, BARCD_NO, SET_SIZE, TIRE_FLOW, " +
                        "TEST_ITEM_NAME, RIM_SIZE, AIR_PRSS, PGS_STATUS, UDT_DTM FROM IF_TEST_PLN_V ORDER BY REQ_NO,PGS_STATUS,UDT_DTM desc")
                .build();
    }

    @Bean
    public ItemWriter<TestPlan> findAllPlanWriter() {
        return list -> {
            for (TestPlan testPlan : list) {
                System.out.println(ifTestPlanBack.toString() + "----ddd");
                testAllPlanService.testPlanProcess(testPlan, ifTestPlanBack);
            }
        };
    }

    @Bean
    @JobScope
    public Step findMaxResvNumber(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("findMaxResvNumber")
                .<TestSchedule, TestSchedule>chunk(chunckSize)
                .reader(selectMaxNumber())
                .writer(saveMaxNumber())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<TestSchedule> selectMaxNumber() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        return new JdbcCursorItemReaderBuilder<TestSchedule>()
                .name("selectMaxNumber")
                .fetchSize(chunckSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TestSchedule.class))
                .sql( "SELECT case (count(TC_RESERV_CODE)) when 0 then '1' else" +
                        "(select TC_RESERV_CODE from TEST_SCHDULE where TC_RESERV_CODE like 'T"+today+"H%' ORDER BY TC_SEQ desc limit 1) end as TC_RESERV_CODE " +
                        "FROM TEST_SCHDULE WHERE TC_RESERV_CODE like 'T"+today+"H%'")
                .build();
    }

    @Bean
    public ItemWriter<TestSchedule> saveMaxNumber() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        return list -> {
            for(TestSchedule testTestSchedule : list){
                if(testTestSchedule.getTcReservCode().equals("1")){
                    tcReservationCode = "T"+today+"H000";
                }else{
                    tcReservationCode = testTestSchedule.getTcReservCode();
                }
            }
        };
    }

    @Bean
    @JobScope
    public Step insertTestScheduleStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("insertTestScheduleStep")
                .<TestPlanOrigin, TestPlanOrigin>chunk(chunckSize)
                .reader(selectTestPlanOriginReader())
                .writer(insertTestScheduleSeqToTest())
                .build();
    }

    @Bean
    public JpaPagingItemReader<TestPlanOrigin> selectTestPlanOriginReader(){
        return new JpaPagingItemReaderBuilder<TestPlanOrigin>()
                .name("selectTestPlanOriginReader")
                .entityManagerFactory(originEntityManagerFactory)
                .pageSize(chunckSize)
                .queryString("SELECT h FROM TestPlanOrigin h left outer join h.testCar c")
                .build();
    }

    public ItemWriter<TestPlanOrigin> insertTestScheduleSeqToTest() {
        return list -> {
            for(TestPlanOrigin tpo : list){
                String planDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                //step2 take : TEST_PLAN_ORIGIN -> TEST_PACKAGE_MAPPING
                List<TestPackageMapping> packageList = testPackageMappingRepository.findAllByName(tpo.getTestItemName());


                //step1 : TEST_PLAN_ORIGIN -> TEST_SCHEDULE
                TestSchedule checkRegNo = testSchedulerService.findAllByRegNoAndTcDay(tpo.getReqNo(), planDay).orElseGet(TestSchedule::new);

                System.out.println("===================="+checkRegNo);

                if(checkRegNo.getRegNo().equals(NONE)){
                    String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
                    StringBuilder sb = new StringBuilder();
                    int n = Integer.parseInt(tcReservationCode.substring(8)) + 1;
                    tcReservationCode = sb.append("T").append(today).append("H").append(n < 10 ? "00" + n : n < 100 ? "0" + n : String.valueOf(n)).toString();

                    testSchedulerService.insertTestSchedule(planDay, tcReservationCode, tpo);
                }else
                {
                    tpo.setTcSeq(checkRegNo.getTcSeq());    //이미 테스트 스케쥴존재 o planorigin에 넣어줌
                }

                //step2
                packageList.forEach(testPackageMapping -> {
                    testTrackRsvService.insertTrackReservation(planDay, testPackageMapping, tpo);
                });

                //step3
                // 차량
                TestBaminResource testBaminResourceCar = testBaminResourceRepository.findByVhclCode(tpo.getVhclCode() == null ? "" : tpo.getVhclCode()).orElseGet(TestBaminResource::new);
                testResourceMappingService.insertVhclCode(planDay, testBaminResourceCar, tpo);


                //엔지니어
                if(tpo.getEngineerOneNo() != null){
                    TestBaminResource testResource = testBaminResourceRepository.findByEmployeeNo(tpo.getEngineerOneNo()).orElseGet(TestBaminResource::new);
                    TestBaminDriver testDriver = testDriverRepository.findById(tpo.getEngineerOneNo()).orElseGet(TestBaminDriver::new);

                    System.out.println("========="+testResource);
                    System.out.println(testDriver);

                    testResourceMappingService.insertEngineer(planDay, testResource, testDriver, tpo, "ONE");
                }


            }
        };

    };


    @Bean
    @JobScope
    public Step totalTestTireData(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("totalTestTireData")
                .<TestTireTotal,TestTireTotal>chunk(chunckSize)
                .reader(selectTireTestTotal())
                .writer(saveTireTotal())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<TestTireTotal> selectTireTestTotal(){
        return new JdbcCursorItemReaderBuilder<TestTireTotal>()
                .fetchSize(chunckSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TestTireTotal.class))
                .sql(" SELECT hpo.REQ_NO as reqNo, hpo.PLN_DTM as plnDtm, hpo.TEST_ITEM_NAME as testItemName," +
                        "sum(hpo.SET_SIZE)as setSize," +
                        "group_concat(distinct hpo.SPEC_SIZE) as tireSize," +
                        "group_concat(distinct hpo.RIM_SIZE) as wheelSize," +
                        "group_concat(distinct hpo.VHCL_Code) as vhclCode," +
                        "group_concat(distinct phc.VHCL_NAME) as vhclName," +
                        "group_concat(distinct hpo.TIRE_FLOW) as returnScrap," +
                        "hpo.TC_SEQ as tcSeq " +
                        "FROM TEST_PLAN_ORIGIN hpo join TEST_BAMIN_CAR phc on hpo.VHCL_CODE = phc.VHCL_CODE" +
                        "GROUP BY REQ_NO, PLN_DTM, TEST_ITEM_NAME")
                .name("selectTestTireTotal")
                .build();
    }

    @Bean
    public ItemWriter<TestTireTotal> saveTireTotal(){
        return list -> {
            for(TestTireTotal testTireTotal : list) {
                String[] plnDtm = testTireTotal.getPlnDtm().split("-");
                System.out.println("=========PLNDTM" + plnDtm);
            }
        };
    }

    @Bean
    @JobScope
    public Step findVehiclePlanStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("findVehiclePlanStep")
                .<VehiclePlan, VehiclePlan>chunk(chunckSize)
                .reader(findAllVehiclePlanReader())
                .writer(findAllVehiclePlanWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<VehiclePlan> findAllVehiclePlanReader() {
        return new JdbcCursorItemReaderBuilder<VehiclePlan>()
                .fetchSize(chunckSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(VehiclePlan.class))
                .sql("SELECT VHCL_CODE, PLN_DTM_S as plnDtmStart, PLN_DTM_E as plnDtmEnd, EGNR_EPNO as engineer," +
                        "USE_OBJ, TEST_ROAD, DELETE_YN, CRN_DTM, UDT_DTM FROM IF_VHCL_PLN_V ORDER BY UDT_DTM desc")
                .name("findAllVehiclePlanReader")
                .build();
    }

    public ItemWriter<VehiclePlan> findAllVehiclePlanWriter() {
        return list -> {
            for(VehiclePlan vp : list) {
                vehiclePlanService.vehiclePlanProcess(vp);
            }
        };
    }

    @Bean
    @JobScope
    public Step insertEtcScheduleStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("insertEtcScheduleStep")
                .<VehiclePlanOrigin, VehiclePlanOrigin>chunk(chunckSize)
                .reader(selectVehiclePlanOriginReader())
                .writer(insertEtcSchedule())
                .build();
    }

    @Bean
    public JpaPagingItemReader<VehiclePlanOrigin> selectVehiclePlanOriginReader() {
        return new JpaPagingItemReaderBuilder<VehiclePlanOrigin>()
                .name("selectVehiclePlanOriginReader")
                .entityManagerFactory(originEntityManagerFactory)
                .pageSize(chunckSize)
                .queryString("SELECT v FROM VehiclePlanOrigin v join fetch v.baminCar c")
                .build();
    }

    public ItemWriter<VehiclePlanOrigin> insertEtcSchedule() {
        return list -> {
            for (VehiclePlanOrigin vpo : list) {
                if(vpo.getInsertFlag().equals("Y")) {

                    String tcDay = vpo.getPlnDtmStart().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    String tcDayEnd = "";
                    if(vpo.getPlnDtmEnd()==null){
                        tcDayEnd = null;
                    }else{
                        tcDayEnd = vpo.getPlnDtmEnd().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    }
                    String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
                    StringBuilder sb = new StringBuilder();
                    int n = Integer.parseInt(tcReservationCode.substring(8)) + 1;
                    tcReservationCode = sb.append("T").append(today).append("H").append(n < 10 ? "00" + n : n < 100 ? "0" + n : String.valueOf(n)).toString();
                    //INSERT
                    testSchedulerService.insertEtcTestSchedule(tcDay, tcDayEnd, tcReservationCode, vpo);

                    //Step2
                    String[] testRoadList = vpo.getTestRoad().split(",");
                    List<TestTrack> trackList = new ArrayList<>();


                }

            }
        };
    }


}

