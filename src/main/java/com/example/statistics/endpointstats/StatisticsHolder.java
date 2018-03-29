package com.example.statistics.endpointstats;


public interface StatisticsHolder {

    Statistics getStatistics();

    void merge(double newAmount,long timestamp);

    public void reset();
    
}
