package com.example.statistics.endpointstats;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile(value = "StatisticsResetTest-test")
@Configuration
public class PeriodicalStatisticsCleanupScheduleTestConfiguration {
    
    @Bean
    @Primary
    public StatisticsHolder StatisticsHolderSpy(StatisticsHolder statisticsHolder) {
        return Mockito.spy(statisticsHolder);
    }
    
}
