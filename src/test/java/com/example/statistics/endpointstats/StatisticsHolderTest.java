package com.example.statistics.endpointstats;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
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
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class StatisticsHolderTest {

    public StatisticsHolderImpl instance;

    @MockBean
    Clock clockMock;

    @Before
    public void init() {
        instance = new StatisticsHolderImpl(60);
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
    public void canReduceNewAmountIntoStatsAndGetUpdatedStats() {
        final double anAmount = 13.0d;
        final Instant now = Instant.now();
        instance.merge(anAmount, now.toEpochMilli());
        final Statistics statistics = instance.getStatistics();
        assertEquals(anAmount, statistics.getSum(), 0.0d);
        assertEquals(anAmount, statistics.getAvg(), 0.0d);
        assertEquals(anAmount, statistics.getMax(), 0.0d);
        assertEquals(anAmount, statistics.getMin(), 0.0d);
        assertEquals(1l, statistics.getCount());
    }

    @Test
    public void canGetStatsOfLatestNSeconds() {
        final int retentionSeconds = 3;
        final int statsPerQuantum = 1;
        instance = new StatisticsHolderImpl(retentionSeconds);

        final long now = 1478192204112l;
        given(clockMock.instant())
                .willReturn(Clock.fixed(Instant.ofEpochMilli(now), ZoneId.systemDefault()).instant());

        // using out of order timestamps but within window:  timestamp >= (now - retention ) 
        int numberOfTransactions = retentionSeconds * statsPerQuantum;
        IntStream.rangeClosed(0, numberOfTransactions).parallel().forEach(i -> {
            long transactionTimestamp = now - (i * 1000) % retentionSeconds;
            instance.merge(Double.valueOf(String.valueOf(i)), transactionTimestamp);
        });

        final Statistics statistics = instance.getStatistics();
        assertEquals(numberOfTransactions * (numberOfTransactions + 1) / 2, statistics.getSum(), 0.0d);
        assertEquals((numberOfTransactions) / 2.0d, statistics.getAvg(), 0.0d);
        assertEquals(numberOfTransactions, statistics.getMax(), 0.0d);
        assertEquals(0.0d, statistics.getMin(), 0.0d);
        assertEquals(numberOfTransactions + 1, statistics.getCount(), 0.0d);
    }

    @Test
    public void canGetStatsEvenIfOnlyAQuantumIsPopulated() {
        final int retentionSeconds = 3;
        final int statsPerQuantum = 1;
        instance = new StatisticsHolderImpl(60);

        final long now = 1478192204112l;
        given(clockMock.instant())
                .willReturn(Clock.fixed(Instant.ofEpochMilli(now), ZoneId.systemDefault()).instant());

        // out of order timestamps but within window:  timestamp >= (now - retention ) 
        int numberOfTransactions = retentionSeconds * statsPerQuantum;
        IntStream.rangeClosed(0, numberOfTransactions).parallel().forEach(i -> {
            long transactionTimestamp = now - (i * 1000) % retentionSeconds;
            System.out.println(String.format("I'm thread number %s mering value %s for timestamp %s", Thread.currentThread().getName(), i, transactionTimestamp));
            instance.merge(Double.valueOf(String.valueOf(i)), transactionTimestamp);
        });

        final Statistics statistics = instance.getStatistics();
        assertEquals(numberOfTransactions * (numberOfTransactions + 1) / 2, statistics.getSum(), 0.0d);
        assertEquals((numberOfTransactions) / 2.0d, statistics.getAvg(), 0.0d);
        assertEquals(numberOfTransactions, statistics.getMax(), 0.0d);
        assertEquals(0.0d, statistics.getMin(), 0.0d);
        assertEquals(numberOfTransactions + 1, statistics.getCount(), 0.0d);
    }
    
    
    

    @Test
    public void canReduceStatsInAMultithreadedEnvironment() throws InterruptedException {
        final int iterations = 10000;
        final int threads = 10;
        final CountDownLatch countDown = new CountDownLatch(iterations);

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(threads);

        final List<Callable<Integer>> statsReduceRequests = IntStream.rangeClosed(1, iterations).mapToObj(i -> {

            return (Callable<Integer>) () -> {
                System.out.println(String.format("I'm Thread %s working on amount %s", Thread.currentThread().getName(), i));
                instance.merge(i, Instant.now().toEpochMilli());
                countDown.countDown();
                return i;
            };
        }
        ).collect(Collectors.toList());
        newFixedThreadPool.invokeAll(statsReduceRequests);

        try {
            countDown.await(5, TimeUnit.SECONDS);
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
