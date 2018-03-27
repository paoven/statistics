
package com.example.statistics.endpointstats;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class StatisticsHolderTest {
    
    public StatisticsHolder instance;
    
    @Before
    public void init(){
        instance = new StatisticsHolder();
    }
    
    @Test
    public void initialStatisticsCountersAreSetToZero(){
        final Statistics statistics=instance.getStatistics();
        assertEquals(statistics.getSum(),0.0d,0.0d);
        assertEquals(statistics.getAvg(),0.0d,0.0d);
        assertEquals(statistics.getMax(),0.0d,0.0d);
        assertEquals(statistics.getMin(),0.0d,0.0d);
        assertEquals(statistics.getCount(),0l);
    }
    
    
    
    
}
