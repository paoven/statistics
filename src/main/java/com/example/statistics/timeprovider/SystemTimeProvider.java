package com.example.statistics.timeprovider;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider implements TimeProvider {

    @Override
    public Date getTime() {
        return new Date();
    }

}
