package com.demo.interview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 【職責】以可執行方法示範 Java 核心面試題：ThreadLocal、集合效能與 JVM／執行緒概念說明。
 * 【技巧】結合 ThreadLocal 隔離非執行緒安全物件，並對照 ArrayList／LinkedList 隨機存取成本。
 * 【概念】面試範例強調「為什麼」與可觀測差異；正式系統應改用執行緒安全 API 或不可變設計。
 * 【邊界】不負責生產級並發工具選型或完整 GC 調校。
 */
public class JavaCoreExamples {

    // --- (1) ThreadLocal 範例 ---

    /**
     * 每個執行緒獨立的 {@link SimpleDateFormat}，避免該類別非執行緒安全造成的格式錯亂。
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 以目前執行緒專屬的日期格式器格式化時間，避免多執行緒共用 {@link SimpleDateFormat}。
     *
     * @param date 待格式化的時間
     * @return {@code yyyy-MM-dd HH:mm:ss} 字串
     */
    public String formatDate(Date date) {
        return dateFormatThreadLocal.get().format(date);
    }

    /**
     * 回傳目前執行緒名稱，便於對照 ThreadLocal／多執行緒示範的執行脈絡。
     *
     * @return 目前執行緒名稱
     */
    public String getThreadNameFromContext() {
        return Thread.currentThread().getName();
    }

    // --- (2) 集合時間複雜度比較 ---

    /**
     * 對照 ArrayList（隨機存取約 O(1)）與 LinkedList（約 O(n)）在中段 get 的耗時差異，結果輸出至標準輸出。
     * 僅供面試講解，非嚴謹基準測試。
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
