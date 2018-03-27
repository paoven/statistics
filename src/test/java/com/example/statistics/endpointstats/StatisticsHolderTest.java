package com.example.statistics.endpointstats;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class StatisticsHolderTest {

    public StatisticsHolder instance;

    @Before
    public void init() {
        instance = new StatisticsHolder();
    }

    @Test
    public void initialStatisticsCountersHaveDefaultValues() {
        final Statistics statistics = instance.getStatistics();
        assertEquals(0.0d, statistics.getSum(), 0.0d);
        assertEquals(0.0d, statistics.getAvg(), 0.0d);
        assertEquals(Double.MIN_VALUE, statistics.getMax(), 0.0d);
        assertEquals(Double.MAX_VALUE, statistics.getMin(), 0.0d);
        assertEquals(0.0d, statistics.getCount(), 0l);
    }

    @Test
    public void canMergeNewAmountIntoStatsAndGetUpdatedStats() {
        final double anAmount = 13.0d;
        instance.merge(13.0d);
        final Statistics statistics = instance.getStatistics();
        assertEquals(13.0d, statistics.getSum(), 0.0d);
        assertEquals(13.0d, statistics.getAvg(), 0.0d);
        assertEquals(13.0d, statistics.getMax(), 0.0d);
        assertEquals(13.0d, statistics.getMin(), 0.0d);
        assertEquals(1l, statistics.getCount());
    }

    @Test
    public void canResetStats() {
        final double anAmount = 13.0d;
        instance.merge(13.0d);
        instance.reset();
        final Statistics statistics = instance.getStatistics();
        assertEquals(0.0d, statistics.getSum(), 0.0d);
        assertEquals(0.0d, statistics.getAvg(), 0.0d);
        assertEquals(Double.MIN_VALUE, statistics.getMax(), 0.0d);
        assertEquals(Double.MAX_VALUE, statistics.getMin(), 0.0d);
        assertEquals(0.0d, statistics.getCount(), 0l);
    }

}
