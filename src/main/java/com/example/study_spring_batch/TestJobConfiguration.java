
package com.example.study_spring_batch;


import com.example.study_spring_batch.domain.*;
import com.example.study_spring_batch.repository.TestBaminResourceRepository;
import com.example.study_spring_batch.repository.TestDriverRepository;
import com.example.study_spring_batch.repository.TestResourceRepository;
//import com.example.study_spring_batch.service.TestAllPlanService;
import com.example.study_spring_batch.service.TestAllPlanService2;
import com.example.study_spring_batch.service.TestResourceMappingService;
import com.example.study_spring_batch.service.TestSchedulerService;
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

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories
public class TestJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory originEntityManagerFactory;  //DB 당 하나씩사용

    private final TestAllPlanService2 testAllPlanService2;
    private final TestSchedulerService testSchedulerService;
    private final TestResourceMappingService testResourceMappingService;
    private final DataSource dataSource;

    private final TestResourceRepository testResourceRepository;
    private final TestDriverRepository testDriverRepository;
    private final TestBaminResourceRepository testBaminResourceRepository;

    private static final String NONE = "NONE";
    private final int chunckSize = 1;
    private static String tcReservationCode = "T220101H000";


    @Bean
    public Job testJob(){
        return jobBuilderFactory.get("testJob")
                .start(findAllTestPlan(null))   //시험 계획정보 EAI 연동
                .next(findMaxResvNumber(null)) //테스트 스케쥴에 저장된 가장 최근 ReservationCode 조회
                .next(insertTestScheduleStep(null))// 조회한 시험 계획정보 INSERT
                //.next(totalTestTireData(null))  //타이어 집계
                .build();
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
                        "TEST_ITEM_NAME, RIM_SIZE, AIR_PRSS, PGS_STATUS, UDT_DTM FROM IF_TEST_PLN ORDER BY REQ_NO,PGS_STATUS,UDT_DTM desc")
                .build();
    }

    @Bean
    public ItemWriter<TestPlan> findAllPlanWriter() {
        return list -> {
            for (TestPlan testPlan : list) {
                testAllPlanService2.testPlanProcess(testPlan);

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
    } //테스트

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
//                .faultTolerant()
//                .skip(IllegalArgumentException.class) //IllegalArgumentException 발생 시 skip함
//                .skipLimit(10)
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
                .queryString("SELECT h FROM TestPlanOrigin h join fetch h.testCar c")
                .build();
    }

    public ItemWriter<TestPlanOrigin> insertTestScheduleSeqToTest() {
        return list -> {
            for(TestPlanOrigin tpo : list){
                String planDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                //step2 : TEST_PLAN_ORIGIN -> TEST_PACKAGE_MAPPING
                //우선생략


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
                    tpo.setTcSeq(checkRegNo.getTcSeq());    //이미 테스트 스케쥴에 존재할경우 계속 tcseq를 넣어준다
                }


                //step3
                if(tpo.getEngineerOneNo() != null){
                    TestResource testResource = testBaminResourceRepository.findByEmployeeNo(tpo.getEngineerOneNo()).orElseGet(TestResource::new);  //
                    TestDriver testDriver = testDriverRepository.findById(tpo.getEngineerOneNo()).orElseGet(TestDriver::new);

                    System.out.println("========="+testResource);
                    System.out.println("oooooooooo"+testDriver);
                    testResourceMappingService.insertEngineer(planDay, testResource, testDriver, tpo, "ONE");
                }


            }
        };

    };

    /*
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
                        "FROM TEST_PLAN_ORIGIN hpo join PG_TEST_CAR phc on hpo.VHCL_CODE = phc.VHCL_CODE" +
                        "GROUP BY REQ_NO, PLN_DTM, TEST_ITEM_NAME")
                .name("selectTestTireTotal")
                .build();
    }

    @Bean
    public ItemWriter<TestTireTotal> saveTireTotal(){
        return list -> {
            for(TestTireTotal testTireTotal : list) {
                String[] plnDtm = testTireTotal.getPlnDtm().split("-");
                System.out.println("=========" + plnDtm);
            }
        };
    }
    */

}

