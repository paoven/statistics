package com.example.statistics.endpointstats;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StatisticsHolderImpl implements StatisticsHolder {

    private static final String STATS_KEY = "stats";
    private final ConcurrentHashMap<String, Statistics> hashMap = new ConcurrentHashMap<>();

    public StatisticsHolderImpl() {
        hashMap.put(STATS_KEY, new Statistics());
    }

    @Override
    public Statistics getStatistics() {
        return hashMap.get(STATS_KEY);
    }

    @Override
    public void merge(double newAmount) {
        hashMap.merge(STATS_KEY, Statistics.of(newAmount), (holder,toBeMerged) -> {
             holder.accumulate(toBeMerged);
             return holder;
        });
    }

    @Override
    public void reset() {
          hashMap.merge(STATS_KEY, Statistics.of(0.0d), (holder,toBeMerged) -> {
             holder.reset();
             return holder;
        });
    }

}
