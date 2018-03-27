package com.example.statistics.controllers;

import com.example.statistics.endpointstats.StatisticsHolder;
import java.time.Clock;
import java.time.Instant;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
class TransactionController {
    
    @Autowired
    private Clock clock;
    @Autowired
    private StatisticsHolder statisticsHolder;
    
    @Value("${transaction.age.max.seconds}")
    private long maxTransactionAgeInSeconds;
    
    @RequestMapping(method = RequestMethod.POST, path = "/transactions",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity recordTransaction(@Valid @RequestBody TransactionRecordRequest transaction){
        if(!isTransactionOutdated(transaction.getTimestamp())){
          statisticsHolder.merge(transaction.getAmount());
          return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    private boolean isTransactionOutdated(long transactionTimeStamp){
        return Instant.ofEpochMilli(transactionTimeStamp).isBefore(
            clock.instant().minusSeconds(maxTransactionAgeInSeconds)
        );
    }
    
}
