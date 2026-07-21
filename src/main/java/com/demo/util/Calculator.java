package com.demo.util;

/**
 * 【職責】提供基礎四則運算，供單元測試與 {@code /api/test/calculator} 驗證串接。
 * 【技巧】以純 Java 方法表達運算，不依賴 Spring 容器。
 * 【概念】把可測的純函式與 Web／持久化分離，可先用單元測試驗證行為再接到 API。
 * 【邊界】不含金融精度、溢位策略或訂單金額計算。
 */
public class Calculator {

    /**
     * 【職責】回傳兩整數之和。
     * 【技巧】直接使用語言內建加法運算子。
     * 【概念】示範最簡單的可測行為；真實金額應改用 BigDecimal。
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * 【職責】回傳兩整數之差。
     * 【技巧】直接使用語言內建減法運算子。
     * 【概念】與加法相同，作為對稱的單元測試範例。
     */
    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * 【職責】回傳兩整數之積。
     * 【技巧】直接使用語言內建乘法運算子。
     * 【概念】乘法溢位在 int 範圍內可能靜默發生；教學上先聚焦可測契約。
     */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * 【職責】執行整數除法並以 {@code double} 回傳商。
     * 【技巧】先檢查除數為零再轉型相除，失敗時拋 {@link ArithmeticException}。
     * 【概念】除零是明確的契約錯誤；以例外表達比回傳魔術數字更不易被忽略。
     * @throws ArithmeticException 當 {@code b == 0}
     */
    public double divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("除數不能為零");
        return (double) a / b;
    }
}
