package com.example.statistics.endpointstats;

import java.util.concurrent.ConcurrentHashMap;

public class StatisticsHolder {

    private static final String STATS_KEY = "stats";
    private final ConcurrentHashMap<String, Statistics> hashMap = new ConcurrentHashMap<>();

    public StatisticsHolder() {
        hashMap.put(STATS_KEY, new Statistics());
    }

    public Statistics getStatistics() {
        return hashMap.get(STATS_KEY);
    }

    public void merge(double newAmount) {
        hashMap.merge(STATS_KEY, Statistics.of(newAmount), (holder,toBeMerged) -> {
             holder.accumulate(toBeMerged);
             return holder;
        });
    }

    public void reset() {
          hashMap.merge(STATS_KEY, Statistics.of(0.0d), (holder,toBeMerged) -> {
             holder.reset();
             return holder;
        });
    }

}
