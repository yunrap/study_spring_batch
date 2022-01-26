package com.example.study_spring_batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 10;
    private final DataSource dataSource;

    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .build();
    }


    @Bean
    public Step helloStep1(){
        return stepBuilderFactory.get("helloStep1")
                .<Car, Car>chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Car> customItemReader() {
            return new JdbcCursorItemReaderBuilder<Car>()
                    .name("JdbcCursorItemReader")
                    .fetchSize(chunkSize)
                    .sql("select VHCL_CODE, VHCL_RGSNO, VHCL_MAKER, VHCL_NAME, VHCL_CLR, MING_FG, CRN_DTM, UDT_DTM FROM PG_HINT_CAR WHERE VHCL_MAKER like ? ")
                    .beanRowMapper(Car.class)
                    .queryArguments("현%")
                    .dataSource(dataSource)
                    .build();
    }

    @Bean
    public ItemWriter<Car> customItemWriter(){
        return items -> {
            for(Car item : items){
                System.out.println(item.toString());
            }
        };
    }
}
