
package com.example.statistics.controllers;

import com.example.statistics.endpointstats.Statistics;
import com.example.statistics.endpointstats.StatisticsHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.*;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StatisticsHolder statisticsHolderMock;
    

    @Test
    public void canRetrieveStatistics() throws Exception {
        final Statistics toBeReturnedStats = new Statistics();
        
        given(statisticsHolderMock.getStatistics())
                .willReturn(toBeReturnedStats);

        this.mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{'sum': 0.0, 'avg': 0.0, 'max': %s, 'min': %s, 'count': 0}",Double.MIN_VALUE,Double.MAX_VALUE),true));
        
    }
}