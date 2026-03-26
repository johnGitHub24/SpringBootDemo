package com.demo.java21;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Java 21 核心特性學習範例
 * 包含：虛擬線程 (Virtual Threads)、Record Patterns、Pattern Matching for switch、Sequenced Collections
 */
public class Java21FeaturesDemo {

    /**
     * 1. 虛擬線程 (Virtual Threads) - Project Loom
     * 虛擬線程是輕量級線程，大大降低了編寫、維護和觀察高吞吐量並發應用程式的門檻。
     */
    public long virtualThreadsDemo(int taskCount) {
        long startTime = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    try {
                        // 模擬阻塞操作
                        Thread.sleep(Duration.ofMillis(10));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } // executor.close() 會等待所有任務完成
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 2. Record Patterns
     * 允許解構 Record 以提取其組件，簡化了數據處理。
     */
    public String recordPatternsDemo(Object obj) {
        record Point(int x, int y) {}
        record ColoredPoint(Point p, String color) {}

        if (obj instanceof ColoredPoint(Point(int x, int y), String c)) {
            return String.format("顏色: %s, 坐標: (%d, %d)", c, x, y);
        }
        return "不匹配的格式";
    }

    /**
     * 3. Pattern Matching for switch (進階：Guarded Patterns)
     * 使用 'when' 子句來增加額外的邏輯判斷。
     */
    public String switchPatternMatchingAdvancedDemo(Object obj) {
        return switch (obj) {
            case Integer i when i > 100 -> "超級大整數";
            case Integer i when i < 0   -> "負整數";
            case Integer i              -> "普通整數";
            case String s when s.contains("Important") -> "重要訊息: " + s;
            case String s when s.isEmpty() -> "空字串";
            case String s               -> "一般字串";
            default                     -> "其他類型";
        };
    }

    /**
     * 4. Sequenced Collections
     * 引入了新的接口來表示具有確定的遇到順序 (encounter order) 的集合。
     */
    public List<String> sequencedCollectionsDemo() {
        LinkedHashSet<String> list = new LinkedHashSet<>();
        list.add("中間");
        list.addFirst("開頭");
        list.addLast("結尾");

        List<String> result = new ArrayList<>();
        result.add(list.getFirst()); // 獲取第一個
        result.add(list.getLast());  // 獲取最後一個
        
        // 獲取反轉視圖並轉換回 List
        result.addAll(list.reversed().stream().toList());
        
        return result;
    }
}
