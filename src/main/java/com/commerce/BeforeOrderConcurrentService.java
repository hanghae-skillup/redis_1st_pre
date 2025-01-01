package com.commerce;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeforeOrderConcurrentService {

    private final Map<String, Integer> productDatabase = new ConcurrentHashMap<>();
    // 가장 최근 주문 정보를 저장하는 DB
    private final Map<String, OrderInfo> latestOrderDatabase = new ConcurrentHashMap<>();

    public BeforeOrderConcurrentService() {
        // 초기 상품 데이터
        productDatabase.put("apple", 100);
        productDatabase.put("banana", 50);
        productDatabase.put("orange", 75);
    }

    // 주문 처리 메서드
    public void order(String productName, int amount) {
        productDatabase.compute(productName, (key, currentStock) -> {
            // 현재 재고가 존재하지 않거나, 남은 재고가 주문하려는 `amount`보다 적다면 현재 재고를 반환한다.
            if (currentStock == null || currentStock < amount) {
                return currentStock;
            }
            System.out.println("Current Thread : " + Thread.currentThread().getName() +
                    " - CurrentStock : " + currentStock + " - Order : " + amount);
            latestOrderDatabase.put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
            return currentStock - amount;
        });
    }

    // 재고 조회
    public int getStock(String productName) {
        return productDatabase.getOrDefault(productName, 0);
    }

}
