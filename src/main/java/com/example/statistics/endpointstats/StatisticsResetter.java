package com.example.statistics.endpointstats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsResetter {

    @Autowired
    StatisticsHolder statisticsHolder;

    @Scheduled(fixedRateString = "${statistics.window.slide.interval.milliseconds}")
    public void slideStatisticsWindow() {
        statisticsHolder.slideStatisticsWindow();
    }
}
