
package com.example.statistics.controllers;

import com.example.statistics.endpointstats.StatisticsHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StatisticsController {
        
    @Autowired
    private StatisticsHolder statisticsHolder;
    @Autowired
    ObjectMapper objectMapper;
    
    @RequestMapping(method = RequestMethod.GET,path = "/statistics",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatisticsResponse> statistics(){
        return ResponseEntity.ok(objectMapper.convertValue(statisticsHolder.getStatistics(), StatisticsResponse.class));
    }
    
}
