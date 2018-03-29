package com.example.statistics.endpointstats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StatisticsHolderImpl implements StatisticsHolder {

    private final static Integer PARALLELISM = Math.max(1, Runtime.getRuntime().availableProcessors()-1);
    private final ConcurrentHashMap<Integer, Statistics> hashMap;

    public StatisticsHolderImpl(@Value("${transaction.age.max.seconds}") int retentionInSeconds) {
        hashMap = new ConcurrentHashMap<>(retentionInSeconds);
    }

    @Override
    public Statistics getStatistics() {
        final Statistics reducedStats = hashMap.<Statistics>reduce(PARALLELISM,
                (Integer t, Statistics quantumStat) -> {
                    try {
                        return quantumStat.clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new IllegalArgumentException(ex.getMessage(),ex);
                    }
                },
                (Statistics reduced, Statistics toBeReduced) -> {
                    reduced.reduce(toBeReduced);
                    return reduced;
                }
        );
        return Optional.ofNullable(reducedStats).orElse(Statistics.zeroedStats());
    }

    @Override
    public void merge(double newAmount, long timestamp) {
        int secondOfMinute = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).getSecond();
        final Statistics statistics = Statistics.of(newAmount);

        hashMap.merge(secondOfMinute, statistics, (holder, toBeMerged) -> {
            holder.reduce(toBeMerged);
            return holder;
        });

    }

    @Override
    public void reset() {
//        hashMap.merge(STATS_KEY, Statistics.of(0.0d), (holder, toBeMerged) -> {
//            holder.reset();
//            return holder;
//        });
    }

}
