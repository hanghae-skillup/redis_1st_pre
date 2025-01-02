package com.example.redis;

public record OrderInfo(String productName, int amount, long currentTimeMillis) {

}
