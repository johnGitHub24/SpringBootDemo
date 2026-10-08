package com.demo.springbootdemo.advanced.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 【職責】訂閱 {@code payment-topic}，將收到的訊息交給內部流式處理流程。
 * <p>【技巧】以 {@code @KafkaListener} 綁定 topic 與 consumer group 作為回呼入口。
 * <p>【概念】Consumer 是訊息邊界；後續轉換／過濾／持久化應下沉，避免 listener 膨脹。
 * <p>【邊界】不負責訊息發送、offset 重放或跨 Topic 路由。
 */
@Slf4j
@Service
public class KafkaConsumerService {

    /**
     * 監聽支付主題並觸發後續流式處理；此方法為 Spring Kafka 回呼入口。
     *
     * @param message 自 Kafka 收到的原始字串 Payload
     */
    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void listenPayment(String message) {
        log.info("收到 Kafka 支付訊息: {}", message);
        
        // 進入流式處理流程 (Transform -> Filter -> Persist)
        processStream(message);
    }

    /**
     * 模擬流式處理邏輯
     * <br>在高併發場景下，這裡通常會進行非同步處理或批次寫入資料庫
     * @param rawData 原始數據
     */
    private void processStream(String rawData) {
        log.info("正在執行訊息流式處理 (Streaming Process)...");
        // 範例：將數據轉為大寫並模擬後續處理
        String processedData = rawData.toUpperCase();
        log.info("處理後的數據已準備持久化: {}", processedData);
    }
}
