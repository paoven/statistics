package com.example.statistics.endpointstats;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("StatisticsResetTest-test")
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"transaction.age.max.seconds=1"})
public class StatisticsResetTest {

    @Autowired
    StatisticsHolder statisticsHolder;
    @Value("${transaction.age.max.milliseconds}")
    Long resetPeriodInMillis;


    @Test
    public void statisticsResetIsPeriodicallyCalled() throws InterruptedException {
        int resetCycles = 2;
        Thread.sleep(resetPeriodInMillis*resetCycles+1);
        verify(statisticsHolder,atLeast(resetCycles)).reset();
    }
}
