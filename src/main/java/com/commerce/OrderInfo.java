package com.commerce;

public class OrderInfo {

    private String productName;
    private int amount;
    private long time;

    public OrderInfo(String productName, int amount, long time) {
        this.productName = productName;
        this.amount = amount;
        this.time = time;
    }
}
