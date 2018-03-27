package com.example.statistics.endpointstats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class StatisticsResetter {
    
    @Autowired
    StatisticsHolder statisticsHolder;
    
    @Scheduled(fixedRateString = "${transaction.age.max.milliseconds}")
    public void resetStatistics(){
        statisticsHolder.reset();
    }
}
