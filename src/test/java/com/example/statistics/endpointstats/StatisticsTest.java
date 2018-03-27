package com.example.statistics.endpointstats;

import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StatisticsTest {

    @Test
    public void initialMaxIs0() {
        final Statistics statistics = new Statistics();
        assertEquals(0.0d, statistics.getMax(), 0.0d);
    }

    @Test
    public void initialMinIs0() {
        final Statistics statistics = new Statistics();
        assertEquals(0.0d, statistics.getMax(), 0.0d);
    }

    @Test
    public void maxIsUpdatedAfterMerge() {
        double amount = 13.0d;
        final Statistics statistics = new Statistics();
        statistics.accumulate(Statistics.of(amount));
        assertEquals(amount, statistics.getMax(), 0.0d);
    }

    @Test
    public void minIsUpdatedAfterMerge() {
        double amount = 13.0d;
        final Statistics statistics = new Statistics();
        statistics.accumulate(Statistics.of(amount));
        double secondAmount = 12.0d;
        statistics.accumulate(Statistics.of(secondAmount));
        assertEquals(secondAmount, statistics.getMin(), 0.0d);
    }

    @Test
    public void countIsUpdatedAfterMerge() {
        int numberOfMerges = 5;
        final Statistics statistics = new Statistics();
        IntStream.rangeClosed(1, numberOfMerges).forEach(i -> {
            statistics.accumulate(Statistics.of(i));
        });
        assertEquals(numberOfMerges, statistics.getCount());
    }

    @Test
    public void avgIsUpdatedAfterMerge() {
        double amount = 13.0d;
        double secondAmount = 12.0d;
        final Statistics statistics = new Statistics();
        statistics.accumulate(Statistics.of(amount));
        statistics.accumulate(Statistics.of(secondAmount));
        assertEquals((amount + secondAmount) / 2, statistics.getAvg(), 0.0d);
    }
}
