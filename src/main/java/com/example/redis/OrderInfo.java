package com.example.redis;

public class OrderInfo {

    private final String productName;
    private final int amount;
    private final long currentTimeMillis;

    public OrderInfo(String productName, int amount, long currentTimeMillis) {
        this.productName = productName;
        this.amount = amount;
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getProductName() {
        return productName;
    }

    public int getAmount() {
        return amount;
    }

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }
}
