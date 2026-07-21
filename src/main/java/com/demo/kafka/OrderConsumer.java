package com.demo.kafka;

import com.demo.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 【職責】監聽 topic {@code order-events}，記錄消費結果供測試驗證。
 * 【技巧】以 {@code @KafkaListener} 綁定 topic 與 consumer group，收到訊息後寫入測試佇列。
 * 【概念】Consumer 是訊息入口；把觀測與業務副作用分開，可讓重試策略獨立演進。
 * 【邊界】不回寫訂單狀態、不實作死信佇列。
 */
@Component
public class OrderConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    /**
     * 【職責】消費單筆訂單事件並記錄摘要供驗證端點讀取。
     * 【技巧】依賴 Spring Kafka 反序列化為 {@link Order}，再呼叫 {@link com.demo.controller.TestController#addConsumedMessage}。
     * 【概念】listener 應保持短小；長時間工作應非同步化，以免拖慢消費進度。
     * @param order 反序列化後的訂單訊息
     */
    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void consume(Order order) {
        String msg = String.format("Consumed Order: %s at %s", order.getId(), java.time.LocalDateTime.now());
        logger.info("#### -> " + msg);
        com.demo.controller.TestController.addConsumedMessage(msg);
    }
}
