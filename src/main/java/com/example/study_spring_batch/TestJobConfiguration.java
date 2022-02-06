package com.example.study_spring_batch;


import com.example.study_spring_batch.domain.TestPlan;
import com.example.study_spring_batch.domain.TestPlanOrigin;
import com.example.study_spring_batch.domain.TestTireTotal;
import com.example.study_spring_batch.service.TestAllPlanService;
import com.example.study_spring_batch.service.TestTireService;




import lombok.RequiredArgsConstructor;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.format.DateTimeFormatter;


@RequiredArgsConstructor
@Configuration
public class TestJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;


    private final TestAllPlanService testAllPlanService;
    //private final TestTireService testTireService;

    private final DataSource dataSource;
    private final int chunckSize = 1;


    @Bean
    public Job testJob(){
        return jobBuilderFactory.get("testJob")
                .start(findAllTestPlan(null))   //시험
                .next(totalTestTireData(null))  //타이어 집계
                .build();
    }

    @Bean
    @JobScope
    public Step findAllTestPlan(@Value("#{jobParameters[requestDate]}") String requestDate){
        return stepBuilderFactory.get("findAllTestPlan")
                .<TestPlanOrigin,TestPlanOrigin>chunk(chunckSize)
                .reader(findAllTestReader())
                .writer(findAllPlanWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<TestPlan> findAllTestReader() {
        return new JdbcCursorItemReaderBuilder<TestPlan>()
                .fetchSize(chunckSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TestPlan.class))
                .sql("SELECT REQ_NO, PLN_DTM, EGNR_EPNO_1 as engineerOne, EGNR_EPNO_2 as engineerTwo, VHCL_CODE, SPEC_SIZE, BARCD_NO, SET_SIZE, TIRE_FLOW, " +
                        "TEST_ITEM_NAME, RIM_SIZE, AIR_PRSS, PGS_STATUS, UDT_DTM FROM IF_TEST_PLN ORDER BY REQ_NO,PGS_STATUS,UDT_DTM desc")
                .name("findAllPlanReader")
                .build();
    }

    @Bean
    public ItemWriter<TestPlan> findAllPlanWriter() {
        return list -> {
            for (TestPlan testPlan : list) {
                testAllPlanService.testPlanProcess(testPlan);
            }
        };
    }



    /*타이어집계학인*/
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


}
