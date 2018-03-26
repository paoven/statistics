package com.example.statistics.controllers;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
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
@WebMvcTest(TransactionController.class)
public class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    Clock clockMock;

    @Test
    public void transactionInTheLast60SecondsYields201() throws Exception {
        final long exampleTimestamp = 1478192204000l;
        
        given(clockMock.instant())
                .willReturn(Clock.fixed(Instant.ofEpochMilli(exampleTimestamp), ZoneId.systemDefault()).instant());

        final String validTransactionRequest = String.format("{\n" +
                                                  "\"amount\": 12.3,\n" +
                                                   "\"timestamp\": %s\n" +
                                                 "}",exampleTimestamp);

        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON).content(validTransactionRequest))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}
