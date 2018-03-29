package com.example.statistics.endpointstats;

import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StatisticsTest {

    @Test
    public void initialMaxIs0() {
        final Statistics statistics = Statistics.zeroedStats();
        assertEquals(0.0d, statistics.getMax(), 0.0d);
    }

    @Test
    public void initialMinIs0() {
        final Statistics statistics = Statistics.zeroedStats();
        assertEquals(0.0d, statistics.getMax(), 0.0d);
    }

    @Test
    public void maxIsUpdatedAfterReduce() {
        double amount = 13.0d;
        final Statistics statistics = Statistics.zeroedStats();
        statistics.reduce(Statistics.of(amount));
        assertEquals(amount, statistics.getMax(), 0.0d);
    }

    @Test
    public void minIsUpdatedAfterReduce() {
        double amount = 13.0d;
        final Statistics statistics = Statistics.of(amount);
        double secondAmount = 12.0d;
        statistics.reduce(Statistics.of(secondAmount));
        assertEquals(secondAmount, statistics.getMin(), 0.0d);
    }

    @Test
    public void countIsUpdatedAfterReduce() {
        int numberOfReduces = 5;
        final Statistics statistics = Statistics.zeroedStats();
        IntStream.rangeClosed(1, numberOfReduces).forEach(i -> {
            statistics.reduce(Statistics.of(i));
        });
        assertEquals(numberOfReduces, statistics.getCount());
    }

    @Test
    public void avgIsUpdatedAfterReduce() {
        double amount = 13.0d;
        double secondAmount = 12.0d;
        final Statistics statistics = Statistics.of(amount);
        statistics.reduce(Statistics.of(secondAmount));
        assertEquals((amount + secondAmount) / 2, statistics.getAvg(), 0.0d);
    }

    @Test
    public void canCloneAStatistic() throws CloneNotSupportedException {
        double amount = 13.0d;
        final Statistics statistics = Statistics.of(amount);
        final Statistics cloned = statistics.clone();
        assertTrue(statistics != cloned);
        assertEquals(statistics, cloned);
    }

}
