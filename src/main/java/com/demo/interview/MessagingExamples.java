package com.demo.interview;

/**
 * Group F: 消息佇列技術範例
 * 比較 Kafka 與 RabbitMQ，並說明其交換器模式。
 */
public class MessagingExamples {

    // --- (1) Kafka vs RabbitMQ 架構差異 (說明為主) ---

    /*
     * [RabbitMQ]
     * - 代理人模型 (Broker-centric): 代理人監控訊息是否送達。
     * - 訊息在被取用後即刪除 (除非手動 ACK 管理)。
     * - 支持訊息優先級。
     * - 延遲低。
     * 
     * [Kafka]
     * - 基於日誌 (Log-centric): 訊息發布後保存在 Log 中，不管消費者是否處理。
     * - 消費者追蹤偏移量 (Offset) 來決定下一條訊息。
     * - 基於保留政策保留訊息 (適合數據回溯、大數據吞吐)。
     * - 高吞吐量。
     */

    // --- (2) RabbitMQ 交換器 (Exchange) 類型 ---

    /*
     * 1. Direct: 完全匹配路由鍵 (Routing Key)。
     * 2. Topic: 模糊匹配路由鍵 (# 為一至多詞, * 為單詞)。
     * 3. Fanout: 廣播模式，忽視路由鍵，轉發到所有綁定的佇列。
     * 4. Headers: 根據訊息的 Header 內容來匹配路由。
     */

    // --- (3) RabbitMQ Topic 模式示例 ---

    /**
     * 模擬 RabbitMQ 的路由邏輯。
     */
    public String routeMessageWithTopic(String routingKey, String bindingPattern) {
        // 修正後的模擬邏輯：
        // 1. 將 '.' 轉義為正則點
        // 2. 將 '*' 轉為匹配單個單詞 (不含點) 的正則 [^.]+
        // 3. 將 '#' 轉為匹配零個或多個單詞的正則 (.*)
        String regex = bindingPattern
                .replace(".", "\\.")
                .replace("*", "([^.]+)")
                .replace("#", "(.*)");
        
        // 增加邊界錨點，避免部分匹配。同時處理 '#' 在末尾時的點號問題
        // 例如 "usa.#" 應匹配 "usa"
        if (bindingPattern.endsWith(".#") && routingKey.equals(bindingPattern.substring(0, bindingPattern.length() - 2))) {
            return "訊息匹配成功! 發送到佇列";
        }

        return routingKey.matches(regex) ? "訊息匹配成功! 發送到佇列" : "路由鍵不匹配";
    }
}
