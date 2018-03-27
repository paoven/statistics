package com.example.statistics.endpointstats;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class StatisticsHolderTest {

    public StatisticsHolderImpl instance;

    @Before
    public void init() {
        instance = new StatisticsHolderImpl();
    }

    @Test
    public void initialStatisticsCountersHaveDefaultValues() {
        final Statistics statistics = instance.getStatistics();
        assertEquals(0.0d, statistics.getSum(), 0.0d);
        assertEquals(0.0d, statistics.getAvg(), 0.0d);
        assertEquals(0.0d, statistics.getMax(), 0.0d);
        assertEquals(0.0d, statistics.getMin(), 0.0d);
        assertEquals(0.0d, statistics.getCount(), 0l);
    }

    @Test
    public void canMergeNewAmountIntoStatsAndGetUpdatedStats() {
        final double anAmount = 13.0d;
        instance.merge(anAmount);
        final Statistics statistics = instance.getStatistics();
        assertEquals(anAmount, statistics.getSum(), 0.0d);
        assertEquals(anAmount, statistics.getAvg(), 0.0d);
        assertEquals(anAmount, statistics.getMax(), 0.0d);
        assertEquals(anAmount, statistics.getMin(), 0.0d);
        assertEquals(1l, statistics.getCount());
    }

    @Test
    public void canResetStats() {
        final double anAmount = 13.0d;
        instance.merge(anAmount);
        instance.reset();
        final Statistics statistics = instance.getStatistics();
        assertEquals(0.0d, statistics.getSum(), 0.0d);
        assertEquals(0.0d, statistics.getAvg(), 0.0d);
        assertEquals(0.0d, statistics.getMax(), 0.0d);
        assertEquals(0.0d, statistics.getMin(), 0.0d);
        assertEquals(0.0d, statistics.getCount(), 0l);
    }

    @Test
    public void canMergeStatsInAMultithreadedEnvironment() throws InterruptedException {
        final int iterations = 1000;
        final int threads = 10;
        final CountDownLatch countDown = new CountDownLatch(iterations);

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(threads);
        final List<Callable<Integer>> statsMergeRequests = IntStream.rangeClosed(1, iterations).mapToObj(i -> {

            return (Callable<Integer>) () -> {
                System.out.println(String.format("I'm Thread %s working on amount %s", Thread.currentThread().getName(), i));
                instance.merge(i);
                countDown.countDown();
                return i;
            };
        }
        ).collect(Collectors.toList());
        newFixedThreadPool.invokeAll(statsMergeRequests);

        try {
            countDown.await(2, TimeUnit.SECONDS);
            final Statistics statistics = instance.getStatistics();
            assertEquals(iterations * (iterations + 1) / 2, statistics.getSum(), 0.0d);
            assertEquals((iterations + 1) / 2.0d, statistics.getAvg(), 0.0d);
            assertEquals(iterations, statistics.getMax(), 0.0d);
            assertEquals(1, statistics.getMin(), 0.0d);
            assertEquals(iterations, statistics.getCount(), 0l);

        } catch (InterruptedException ex) {
            try {
                newFixedThreadPool.shutdownNow();
            } catch (Exception ex2) {
                System.err.println(String.format("An error has occured while shutting down threads: %s", ex2));
            }
        }
    }

}
