package com.example.study_spring_batch;

료import com.example.study_spring_batch.service.TestAllPlanService;
import com.example.study_spring_batch.service.TestSchedulerService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {TestJobConfiguration.class, TestBatchConfig.class, TestAllPlanService.class, TestSchedulerService.class})

@Profile(value = "develop")
public class SimpleJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


   /* private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("requestDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        return paramsBuilder.toJobParameters();
    }*/

    @DisplayName("부분 테스트")
    @Test
    public void simple_Jobtest() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20220131")
                .addLong("date", new Date().getTime())
                .toJobParameters();

        JobExecution jobExecution =  jobLauncherTestUtils.launchJob(jobParameters);
        Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
        Assert.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
    }


}


