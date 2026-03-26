package com.demo.java21;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class Java21FeaturesDemoTest {

    private final Java21FeaturesDemo demo = new Java21FeaturesDemo();

    @Test
    void testVirtualThreads() {
        // 執行 1000 個任務，如果使用傳統線程會造成較大負擔，虛擬線程則非常輕鬆
        long duration = demo.virtualThreadsDemo(1000);
        System.out.println("虛擬線程執行 1000 個任務耗時: " + duration + "ms");
        assertTrue(duration > 0);
    }

    @Test
    void testRecordPatterns() {
        record Point(int x, int y) {}
        record ColoredPoint(Point p, String color) {}

        ColoredPoint cp = new ColoredPoint(new Point(10, 20), "紅色");
        String result = demo.recordPatternsDemo(cp);
        
        assertEquals("顏色: 紅色, 坐標: (10, 20)", result);
        assertEquals("不匹配的格式", demo.recordPatternsDemo("Hello"));
    }

    @Test
    void testSwitchPatternMatching() {
        assertEquals("整數: 100", demo.switchPatternMatchingDemo(100));
        assertEquals("字串: Hello", demo.switchPatternMatchingDemo("Hello"));
        assertEquals("雙精度浮點數: 3.14", demo.switchPatternMatchingDemo(3.14159));
        assertEquals("空值", demo.switchPatternMatchingDemo(null));
    }

    @Test
    void testSequencedCollections() {
        List<String> result = demo.sequencedCollectionsDemo();
        // [開頭, 結尾, 結尾, 中間, 開頭]
        assertEquals("開頭", result.get(0));
        assertEquals("結尾", result.get(1));
        assertEquals("結尾", result.get(2));
        assertEquals("開頭", result.get(4));
    }
}
