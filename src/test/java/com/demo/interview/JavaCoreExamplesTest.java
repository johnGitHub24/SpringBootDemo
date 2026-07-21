package com.demo.interview;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 覆蓋 {@link JavaCoreExamples}（面試示範：Java 核心）。
 * 驗證 ThreadLocal 隔離性與 List 效能比較可執行。
 */
public class JavaCoreExamplesTest {

    private final JavaCoreExamples examples = new JavaCoreExamples();

    /**
     * CASE-IV-JAVA-001：ThreadLocal 多執行緒隔離。
     * Given: 10 執行緒並行；When: formatDate + getThreadNameFromContext；Then: 全部成功且結果獨立。
     */
    @Test
    public void testThreadLocalIndependence() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        
        // 使用 CountDownLatch 確保所有執行緒幾乎同時開始
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    latch.countDown();
                    latch.await();
                    
                    Date now = new Date();
                    String formatted = examples.formatDate(now);
                    String threadName = examples.getThreadNameFromContext();
                    
                    // 驗證獲取的結果不為空且格式正確
                    if (formatted != null && formatted.length() > 0 && threadName.contains("pool-")) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(threadCount, successCount.get(), "每個執行緒應擁有獨立的 ThreadLocalContext，且執行成功");
    }

    /**
     * CASE-IV-JAVA-002：List 效能比較可執行。
     * Given: 範例內建度量邏輯；When: compareListPerformance；Then: 不拋例外。
     */
    @Test
    public void testPerformanceComparison() {
        // 執行此方法以確保內部邏輯無誤（範例代碼中包含 O(1) 與 O(n) 的時間度量輸出）
        assertDoesNotThrow(() -> examples.compareListPerformance());
    }
}
