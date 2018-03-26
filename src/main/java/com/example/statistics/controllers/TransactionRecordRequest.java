package com.example.statistics.controllers;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransactionRecordRequest {

    public TransactionRecordRequest() {
    }

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @NotNull
    @Positive
    private long timestamp; // TODO: extend timestamp validation in order to reject in the future timestamps.

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount.doubleValue();
    }

    public long getTimestamp() {
        return timestamp;
    }

}
