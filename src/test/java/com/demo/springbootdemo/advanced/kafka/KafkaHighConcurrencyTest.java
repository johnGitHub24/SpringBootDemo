package com.demo.springbootdemo.advanced.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 覆蓋 {@link KafkaProducerService} 在高併發下的發送穩定性（訊息層壓力測試）。
 * <br>以多執行緒並行 sendMessage，驗證 30 秒內全部完成。
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 3, topics = {"high-concurrency-topic"})
public class KafkaHighConcurrencyTest {

    @Autowired
    private KafkaProducerService producerService;

    /**
     * CASE-KAFKA-HC-001：50 執行緒 × 100 訊息全部送出。
     * <br>Given: EmbeddedKafka 3 partitions；When: 並行 sendMessage 共 5000 則；Then: latch 於 30 秒內歸零。
     */
    @Test
    public void testHighConcurrencySend() throws InterruptedException {
        int threadCount = 50; // 模擬 50 個並行使用者
        int messagesPerThread = 100; // 每個使用者發送 100 則訊息
        int totalMessages = threadCount * messagesPerThread;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(totalMessages);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            final int userId = i;
            executorService.submit(() -> {
                for (int j = 0; j < messagesPerThread; j++) {
                    try {
                        String message = "User_" + userId + " - Order_" + j;
                        // 呼叫 Service 發送訊息
                        producerService.sendMessage("high-concurrency-topic", "Key_" + userId, message);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        // 等待所有訊息發送完成
        boolean finished = latch.await(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();

        assertThat(finished).isTrue();
        System.out.println(">>> 高併發測試完成！總發送量: " + totalMessages + "，總耗時: " + (endTime - startTime) + "ms");
        
        executorService.shutdown();
    }
}
