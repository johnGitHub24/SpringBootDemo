package com.demo.java21;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import com.demo.java21.Java21FeaturesDemo.ColoredPoint;
import com.demo.java21.Java21FeaturesDemo.Point;

/**
 * 覆蓋 {@link Java21FeaturesDemo}（Java 21 語法示範層）的單元測試。
 * 驗證虛擬執行緒、Record Pattern、Switch Pattern Matching、Sequenced Collections。
 */
public class Java21FeaturesDemoTest {

    private final Java21FeaturesDemo demo = new Java21FeaturesDemo();

    /**
     * CASE-J21-001：虛擬執行緒示範可完成。
     * Given: 1000 個任務；When: virtualThreadsDemo；Then: 耗時大於 0。
     */
    @Test
    void testVirtualThreads() {
        long duration = demo.virtualThreadsDemo(1000);
        System.out.println("虛擬線程執行 1000 個任務耗時: " + duration + "ms");
        assertTrue(duration > 0);
    }

    /**
     * CASE-J21-002：Record Pattern 解構正確。
     * Given: ColoredPoint(10,20,"紅色")；When: recordPatternsDemo；Then: 顏色與坐標字串正確，非匹配回「不匹配的格式」。
     */
    @Test
    void testRecordPatterns() {
        ColoredPoint cp = new ColoredPoint(new Point(10, 20), "紅色");
        String result = demo.recordPatternsDemo(cp);

        assertEquals("顏色: 紅色, 坐標: (10, 20)", result);
        assertEquals("不匹配的格式", demo.recordPatternsDemo("Hello"));
    }

    /**
     * CASE-J21-003：Switch Pattern Matching 分支正確。
     * Given: 整數／字串／其他型別；When: switchPatternMatchingAdvancedDemo；Then: 各分支標籤正確，null 拋 NPE。
     */
    @Test
    void testSwitchPatternMatching() {
        assertEquals("超級大整數", demo.switchPatternMatchingAdvancedDemo(150));
        assertEquals("普通整數", demo.switchPatternMatchingAdvancedDemo(100));
        assertEquals("重要訊息: Important Event", demo.switchPatternMatchingAdvancedDemo("Important Event"));
        assertEquals("一般字串", demo.switchPatternMatchingAdvancedDemo("Hello"));
        assertEquals("其他類型", demo.switchPatternMatchingAdvancedDemo(3.14159));
        assertThrows(NullPointerException.class, () -> demo.switchPatternMatchingAdvancedDemo(null));
    }

    /**
     * CASE-J21-004：Sequenced Collections 頭尾操作正確。
     * Given: 示範序列；When: sequencedCollectionsDemo；Then: 開頭／結尾元素位置符合預期。
     */
    @Test
    void testSequencedCollections() {
        List<String> result = demo.sequencedCollectionsDemo();
        assertEquals("開頭", result.get(0));
        assertEquals("結尾", result.get(1));
        assertEquals("結尾", result.get(2));
        assertEquals("開頭", result.get(4));
    }
}
