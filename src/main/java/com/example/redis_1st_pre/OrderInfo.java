package com.example.redis_1st_pre;

import java.time.LocalDateTime;

public class OrderInfo {
    private final String productName;
    private final int amount;
    private final long timestemp;

    public OrderInfo(String productName, int amount, long timestemp) {
        this.productName = productName;
        this.amount = amount;
        this.timestemp = timestemp;
    }
}
