package com.demo.java21;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 【職責】以可呼叫方法展示 Java 21 虛擬執行緒、Record Patterns、switch 模式比對與 Sequenced Collections。
 * 【技巧】結合 {@code Executors.newVirtualThreadPerTaskExecutor}、record 解構與新集合 API。
 * 【概念】語言新特性應用於真實場景前，先用獨立示範驗證行為與成本差異，學習曲線較可控。
 * 【邊界】不負責生產業務邏輯；僅供學習與單元驗證。
 */
public class Java21FeaturesDemo {

    /**
     * 【職責】提供二維座標 Record，供 Record Patterns 解構示範與測試共用。
     * 【技巧】頂層巢狀 record，避免方法內 local record 造成 instanceof 型別不一致。
     * 【概念】Record 是不可變資料載體；適合當模式比對的目標型別。
     */
    public record Point(int x, int y) {}

    /**
     * 【職責】提供帶顏色的座標 Record，用於巢狀 Record Patterns。
     * 【技巧】組合 {@link Point} 與顏色字串。
     * 【概念】巢狀 record 可一次解構多層欄位，減少手動 getter 鏈。
     */
    public record ColoredPoint(Point p, String color) {}

    /**
     * 【職責】以虛擬執行緒池並行執行大量短延遲任務，回傳總耗時。
     * 【技巧】{@code try-with-resources} 關閉 {@code newVirtualThreadPerTaskExecutor}。
     * 【概念】虛擬執行緒降低阻塞 I/O 的執行緒成本；適合高併發等待，而非 CPU 密集計算。
     */
    public long virtualThreadsDemo(int taskCount) {
        long startTime = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofMillis(10));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 以巢狀 Record Patterns 解構 {@link ColoredPoint}，示範型別與欄位一次比對。
     *
     * @param obj 待比對物件；僅 {@link ColoredPoint} 會成功解構
     * @return 成功時回傳顏色與座標描述；否則回傳不匹配提示
     */
    public String recordPatternsDemo(Object obj) {
        if (obj instanceof ColoredPoint(Point(int x, int y), String c)) {
            return String.format("顏色: %s, 坐標: (%d, %d)", c, x, y);
        }
        return "不匹配的格式";
    }

    /**
     * 以 switch 模式比對（含 guarded patterns）依型別與條件分類輸入。
     *
     * @param obj 任意輸入物件
     * @return 依整數區間或字串內容分類後的描述字串
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
     * 示範 Sequenced Collections 的頭尾插入、讀取與反序走訪能力。
     *
     * @return 依序為：開頭、結尾、以及反序後的完整元素列表
     */
    public List<String> sequencedCollectionsDemo() {
        LinkedHashSet<String> list = new LinkedHashSet<>();
        list.add("中間");
        list.addFirst("開頭");
        list.addLast("結尾");

        List<String> result = new ArrayList<>();
        result.add(list.getFirst());
        result.add(list.getLast());
        result.addAll(list.reversed().stream().toList());

        return result;
    }

    /**
     * 驗證 Record Patterns 能否正確解構已知的 {@link ColoredPoint} 測資。
     *
     * @return 解構結果符合預期時為 {@code true}
     */
    public boolean testRecordPatterns() {
        Object testInput = new ColoredPoint(new Point(3, 5), "藍色");
        if (testInput instanceof ColoredPoint(Point(int x, int y), String c)) {
            return "藍色".equals(c) && x == 3 && y == 5;
        }
        return false;
    }

    /**
     * 驗證 Sequenced Collections 示範結果的頭尾元素是否符合插入順序語意。
     *
     * @return 頭為「開頭」、尾為「結尾」且至少兩元素時為 {@code true}
     */
    public boolean testSequencedCollections() {
        List<String> result = sequencedCollectionsDemo();
        return result.size() >= 2 && "開頭".equals(result.get(0)) && "結尾".equals(result.get(1));
    }
}
