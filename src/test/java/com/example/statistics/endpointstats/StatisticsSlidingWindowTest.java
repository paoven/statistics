package com.example.statistics.endpointstats;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"statistics.window.slide.interval.milliseconds=1000", "transaction.age.max.seconds=2"})
public class StatisticsSlidingWindowTest {

    @Autowired
    StatisticsHolder statisticsHolder;
    @Value("${statistics.window.slide.interval.milliseconds}")
    Long statisticsWindowSlidingIntervalMilliseconds;
    @Value("${transaction.age.max.seconds}")
    Long retentionIntervalSeconds;

    @MockBean
    Clock clockMock;
    @MockBean
    StatisticsResetter statisticsResetter;

    @Test
    public void oldestQuantumIsDeletedAtEveryCleanupCycle() throws InterruptedException {
        Instant time = Instant.ofEpochMilli(1522357545327l);
        given(clockMock.instant()).willReturn(Clock.fixed(time, ZoneId.systemDefault()).instant());

        statisticsHolder.merge(5.0d, time.toEpochMilli() - 2000);
        statisticsHolder.merge(5.0d, time.toEpochMilli() - 2000);
        statisticsHolder.merge(15.0d, time.toEpochMilli() - 2000);
        statisticsHolder.merge(1.0d, time.toEpochMilli() - 1000);

        Statistics reducedBeforeCleanup = statisticsHolder.getStatistics();
        assertEquals(15.0d, reducedBeforeCleanup.getMax(), 0.0d);
        assertEquals(4, reducedBeforeCleanup.getCount());

        time=time.plusMillis(1000l);
        given(clockMock.instant()).willReturn(Clock.fixed(time, ZoneId.systemDefault()).instant());
        statisticsHolder.slideStatisticsWindow();
        Statistics reducedAftetrCleanup = statisticsHolder.getStatistics();
        assertEquals(1.0d, reducedAftetrCleanup.getMax(), 0.0d);
        assertEquals(1, reducedAftetrCleanup.getCount(), 0.0d);

    }
}
