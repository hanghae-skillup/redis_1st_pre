package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeforeOrderServiceJava {
    // 상품 DB -> ConcurrentHashMap 으로 변경
    private final ConcurrentMap<String, Integer> productDatabase = new ConcurrentHashMap<>();
    // 가장 최근 주문 정보를 저장하는 DB -> ThreadLocal 로 변경
    private final ThreadLocal<OrderInfo> latestOrderDatabase = ThreadLocal.withInitial(() -> null);

    public BeforeOrderServiceJava() {
        // 초기 상품 데이터 -> Atomic 으로 변경
        productDatabase.put("apple", 100);
        productDatabase.put("banana", 50);
        productDatabase.put("orange", 75);
    }

    // 주문 처리 메서드
    public void order(String productName, int amount) {
        productDatabase.compute(productName, (key, currentStock) -> {
            if (currentStock == null) {
                System.out.println("상품이 존재하지 않습니다: " + productName);
                return null;
            }

            if (currentStock >= amount) {
                System.out.println("Current Thread : " + Thread.currentThread().getName() +
                        " - CurrentStock : " + currentStock + " - Order : " + amount);
                latestOrderDatabase.set(new OrderInfo(productName, amount, System.currentTimeMillis()));
                return currentStock - amount; // 재고 감소
            } else {
                System.out.println("재고 부족: " + productName + " - 요청량: " + amount + ", 현재 재고: " + currentStock);
                return currentStock; // 재고 유지
            }
        });
    }

    // 재고 조회
    public int getStock(String productName) {
        return productDatabase.getOrDefault(productName, 0);
    }
}
