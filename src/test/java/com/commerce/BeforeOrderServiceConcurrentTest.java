package com.commerce;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BeforeOrderServiceConcurrentTest {

    BeforeOrderConcurrentService service = new BeforeOrderConcurrentService();

    @Test
    void given_when_then() throws InterruptedException {
        String productName = "apple";
        int initialStock = service.getStock(productName);

        int orderAmount = 8;
        int threadCount = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger count = new AtomicInteger(0);

        // 각 스레드에서 주문을 수행하는 작업 생성
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    service.order(productName, orderAmount);
                } finally {
                    latch.countDown(); // 작업 완료 후 카운트 감소
                    count.incrementAndGet();
                }
            });
        }

        // 모든 스레드가 작업을 완료할 때까지 대기
        latch.await();
        executor.shutdown();

        // 최종 재고 값 확인
        int expectedStock = initialStock % orderAmount;
        int actualStock = service.getStock(productName);

        System.out.println("Expected Stock: " + expectedStock + ", Actual Stock: " + actualStock);
        System.out.println("Total order count = " + count.get());

        assertEquals(count.get(), threadCount, "총 주문 건수");

        // 동시성 이슈를 해결하여 예상한 재고와 수량이 같음을 확인하다.
        assertEquals(expectedStock, actualStock, "예상한 제고와 일치!");
    }

}