package com.example.statistics.endpointstats;


public interface StatisticsHolder {

    Statistics getStatistics();

    void merge(double newAmount);

    void reset();
    
}
