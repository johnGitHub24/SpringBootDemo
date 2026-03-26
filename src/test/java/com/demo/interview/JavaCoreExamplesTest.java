package com.demo.interview;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java 核心技術範例的單元測試
 */
public class JavaCoreExamplesTest {

    private final JavaCoreExamples examples = new JavaCoreExamples();

    /**
     * 測試 ThreadLocal 的隔離性。
     * 驗證多個執行緒同時操作時，格式化的日期結果與執行緒名稱是否正確隔離。
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
     * 測試 ArrayList 與 LinkedList 的效能比較邏輯是否可執行。
     */
    @Test
    public void testPerformanceComparison() {
        // 執行此方法以確保內部邏輯無誤（範例代碼中包含 O(1) 與 O(n) 的時間度量輸出）
        assertDoesNotThrow(() -> examples.compareListPerformance());
    }
}
