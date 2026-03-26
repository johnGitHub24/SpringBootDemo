package com.demo.interview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Group A: Java 核心技術範例
 * 涵蓋 ThreadLocal, Collections, JVM 與 執行緒概念。
 */
public class JavaCoreExamples {

    // --- (1) ThreadLocal 範例 ---

    /**
     * 使用 ThreadLocal 來儲存每個執行緒獨立的 SimpleDateFormat 實例。
     * 因為 SimpleDateFormat 是非執行緒安全的 (Non-thread-safe)。
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 格式化日期，每個執行緒會使用自己的 SimpleDateFormat 實例。
     */
    public String formatDate(Date date) {
        return dateFormatThreadLocal.get().format(date);
    }

    /**
     * 獲取當前執行緒的 ThreadLocal 值。
     */
    public String getThreadNameFromContext() {
        return Thread.currentThread().getName();
    }

    // --- (2) 集合時間複雜度比較 ---

    /**
     * 演示 ArrayList 與 LinkedList 的 get 效能差異。
     * ArrayList: O(1)
     * LinkedList: O(n)
     */
    public void compareListPerformance() {
        int items = 100000;
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < items; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        // ArrayList 隨機存取極快
        long start = System.nanoTime();
        arrayList.get(items / 2);
        long end = System.nanoTime();
        System.out.println("ArrayList get O(1): " + (end - start) + " ns");

        // LinkedList 隨機存取需要從頭走訪
        start = System.nanoTime();
        linkedList.get(items / 2);
        end = System.nanoTime();
        System.out.println("LinkedList get O(n): " + (end - start) + " ns");
    }

    // --- (3) JVM 與 多執行緒說明 (寫在註解) ---

    /*
     * [JVM 記憶體區塊說明]
     * 1. Heap (堆): 存放物件實例。分為 Eden, Survivor (S0, S1), Old Gen。
     * 2. Stack (棧): 存放執行緒私有的區域變數、方法呼叫框架。
     * 3. Metaspace (元空間): 存放類別資訊、常數池 (替代了以前的 PermGen)。
     * 
     * [Process vs Thread]
     * - Process (進程): OS 資源分配的獨立單位，記憶體空間隔離。
     * - Thread (執行緒): 進程內的執行路徑，共用進程資源 (Heap, Metaspace)，但有私有 Stack。
     * 
     * [Race Condition (競態條件)]
     * 當多個執行緒同時存取並修改同一個共享變數，且最終結果取決於執行緒執行的順序時發生。
     */
}
