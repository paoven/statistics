package com.example.statistics.endpointstats;

import java.util.concurrent.ConcurrentHashMap;

public class StatisticsHolder {

  private static final String STATS_KEY="stats";  
  private final ConcurrentHashMap<String,Statistics> hashMap = new ConcurrentHashMap<>();
    
  public StatisticsHolder(){
      hashMap.put(STATS_KEY, new Statistics());
  }
  
  public Statistics getStatistics(){
      return hashMap.get(STATS_KEY);
  }

}
