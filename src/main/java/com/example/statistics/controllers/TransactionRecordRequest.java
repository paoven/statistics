
package com.example.statistics.controllers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class TransactionRecordRequest {

    public TransactionRecordRequest() {
    }
 
    @NotNull
    @Min(0)
    private double amount;
    
    @NotNull
    @Min(0)
    private long timestamp;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
}
