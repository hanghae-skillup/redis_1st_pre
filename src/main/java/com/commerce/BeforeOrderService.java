package com.commerce;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BeforeOrderService {

    // 상품 DB
    private final Map<String, Integer> productDatabase = new HashMap<>();
    // 가장 최근 주문 정보를 저장하는 DB
    private final Map<String, OrderInfo> latestOrderDatabase = new HashMap<>();

    private final Lock lock = new ReentrantLock();

    public BeforeOrderService() {
        // 초기 상품 데이터
        productDatabase.put("apple", 100);
        productDatabase.put("banana", 50);
        productDatabase.put("orange", 75);
    }

    // 주문 처리 메서드
    public void order(String productName, int amount) {
        Integer currentStock = productDatabase.getOrDefault(productName, 0);
        try {
            Thread.sleep(1); // 동시성 이슈 유발을 위한 인위적 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        if (currentStock >= amount) {
            System.out.println("Current Thread : " + Thread.currentThread().getName() +
                    " - CurrentStock : " + currentStock + " - Order : " + amount);
            productDatabase.put(productName, currentStock - amount);
            latestOrderDatabase.put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
        }
    }


    public synchronized void orderSync(String productName, int amount) {
        order(productName, amount);
    }
    public void orderSyncObject(String productName, int amount) {
        synchronized (this) {
            order(productName, amount);
        }
    }

    public void orderReentLock(String productName, int amount) {
        lock.lock();                        // 락 획득
        try  {
            order(productName, amount);     // 주문 시도
        } finally {
            lock.unlock();                  // 락 해제
        }
    }

    // 재고 조회
    public int getStock(String productName) {
        return productDatabase.getOrDefault(productName, 0);
    }

}
