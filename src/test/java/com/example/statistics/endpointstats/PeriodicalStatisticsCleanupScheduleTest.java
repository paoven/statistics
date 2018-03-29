package com.example.statistics.endpointstats;

import java.time.Clock;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@ActiveProfiles("StatisticsResetTest-test")
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"statistics.window.slide.interval.milliseconds=100"})
public class PeriodicalStatisticsCleanupScheduleTest {

    @Autowired
    StatisticsHolder statisticsHolder;
    @Value("${statistics.window.slide.interval.milliseconds}")
    Long statisticsWindowSlidingIntervalMilliseconds;


    @Test
    public void statisticsOldestQuantumIsCleanedUpEverySecond() throws InterruptedException {
        int cleanupCycles = 4;
        Thread.sleep(statisticsWindowSlidingIntervalMilliseconds * (cleanupCycles + 1));
        verify(statisticsHolder, atLeast(cleanupCycles)).slideStatisticsWindow();
    }

}
