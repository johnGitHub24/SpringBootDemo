package com.demo.springbootdemo.advanced.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka 訊息消費者服務
 * 
 * 功能：
 * 1. 監聽特定 Topic 的訊息。
 * 2. 實作流式處理 (Streaming Process) 邏輯。
 * 3. 執行數據清洗與初步持久化準備。
 */
@Slf4j
@Service
public class KafkaConsumerService {

    /**
     * 監聽支付主題 (payment-topic)
     * @param message 接收到的原始訊息字串
     */
    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void listenPayment(String message) {
        log.info("收到 Kafka 支付訊息: {}", message);
        
        // 進入流式處理流程 (Transform -> Filter -> Persist)
        processStream(message);
    }

    /**
     * 模擬流式處理邏輯
     * 在高併發場景下，這裡通常會進行非同步處理或批次寫入資料庫
     * @param rawData 原始數據
     */
    private void processStream(String rawData) {
        log.info("正在執行訊息流式處理 (Streaming Process)...");
        // 範例：將數據轉為大寫並模擬後續處理
        String processedData = rawData.toUpperCase();
        log.info("處理後的數據已準備持久化: {}", processedData);
    }
}
