package com.commerce;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BeforeOrderThreadLocalService {

    private final HashMap<String, AtomicInteger> productDatabase = new HashMap<>();
    private final HashMap<String, OrderInfo> latestOrderDatabase = new HashMap<>();
    private final ThreadLocal<OrderInfo> threadLocalOrderInfo = ThreadLocal.withInitial(() -> null);

    public BeforeOrderThreadLocalService() {
        // 초기 상품 데이터
        productDatabase.put("apple", new AtomicInteger(100));
        productDatabase.put("banana", new AtomicInteger(50));
        productDatabase.put("orange", new AtomicInteger(75));
    }

    // 주문 처리 메서드
    public void order(String productName, int amount) {
        // 주문 정보 설정
        threadLocalOrderInfo.set(new OrderInfo(productName, amount, System.currentTimeMillis()));

        AtomicInteger currentStock = productDatabase.get(productName);
        if (currentStock == null || currentStock.get() < amount) {
            return;
        }

        // 재고 감소
        boolean success = currentStock.compareAndSet(currentStock.get(), currentStock.get() - amount);
        if (success) {
            String value = "Current Thread : %s - CurrentStock : %d - Order : %d".formatted(
                    Thread.currentThread().getName(), currentStock.get(), amount);
            System.out.println(value);

            // 최신 주문 정보 업데이트
            latestOrderDatabase.put(productName, threadLocalOrderInfo.get());
        } else {
            System.out.println("Order failed due to concurrent update for " + productName);
        }
    }

    // 재고 조회
    public AtomicInteger getStock(String productName) {
        return productDatabase.getOrDefault(productName, new AtomicInteger(0));
    }

}
