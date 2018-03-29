package com.example.statistics.endpointstats;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StatisticsHolderImpl implements StatisticsHolder {

    private final ConcurrentHashMap<Integer, Statistics> hashMap;

    public StatisticsHolderImpl(@Value("${transaction.age.max.seconds}") int retentionInSeconds) {
        hashMap = new ConcurrentHashMap<>(retentionInSeconds);
    }

    @Override
    public Statistics getStatistics() {
        final Statistics reducedStats = hashMap.<Statistics>reduce(4,
                (Integer t, Statistics quantumStat) -> {
                    return Statistics.of(quantumStat.getSum(),quantumStat.getMax(),quantumStat.getMin(),quantumStat.getCount());
                },
                (Statistics reduced, Statistics toBeReduced) -> {
                    reduced.reduce(toBeReduced);
                    return reduced;
                }
        );
        return Optional.ofNullable(reducedStats).orElse(new Statistics());
    }

    @Override
    public void merge(double newAmount, long timestamp) {
        int secondOfMinute = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC).getSecond();
        final Statistics empty = new Statistics(); empty.accumulate(Statistics.of(newAmount));
        
        hashMap.merge(secondOfMinute, empty, (holder, toBeMerged) -> {
            holder.accumulate(toBeMerged);
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
