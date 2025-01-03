package com.example.redis_1st_pre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockOrderServiceJava {

    private static final Logger log = LoggerFactory.getLogger(LockOrderServiceJava.class);
    Lock lock = new ReentrantLock();

    // 상품 DB
    private final Map<String, Integer> productDatabase = new HashMap<>();
    // 가장 최근 주문 정보를 저장하는 DB
    private final ThreadLocal<Map<String, OrderInfo>> latestOrderDatabase = ThreadLocal.withInitial(HashMap::new);

    public LockOrderServiceJava() {
        // 초기 상품 데이터
        productDatabase.put("apple", 100);
        productDatabase.put("banana", 50);
        productDatabase.put("orange", 75);
    }

    // 주문 처리 메서드
    public void order(String productName, int amount) {
        lock.lock();
        try {
            Integer currentStock = productDatabase.getOrDefault(productName, 0);

            try {
                Thread.sleep(1); // 동시성 이슈 유발을 위한 인위적 지연
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            if (currentStock >= amount) {
                System.out.println("Thread " + Thread.currentThread().threadId() + " 주문 정보: ");
                System.out.println("    " + productName + ": 1 건 ([" + amount + "])");
                productDatabase.put(productName, currentStock - amount);
                latestOrderDatabase.get().put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
            }
        } finally {
            lock.unlock();
        }
    }

    // 재고 조회
    public int getStock(String productName) {
        return productDatabase.getOrDefault(productName, 0);
    }
}