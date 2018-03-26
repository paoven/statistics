
package com.example.statistics.configuration;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainApplicationConfiguration {
    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }
}
