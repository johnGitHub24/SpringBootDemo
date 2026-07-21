package com.demo.kafka;

import com.demo.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 【職責】將訂單變更事件發佈至 Kafka topic {@code order-events}。
 * 【技巧】以 {@link KafkaTemplate} 非同步送出序列化後的訂單本體。
 * 【概念】Producer 只負責「送出」；交易是否成功、消費者如何處理應與發送端解耦。
 * 【邊界】不決定交易提交、不消費訊息；由 {@link com.demo.service.OrderService} 在寫入後呼叫。
 */
@Component
public class OrderProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderProducer.class);
    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 【職責】注入 Kafka 發送範本。
     * 【技巧】建構子注入固定依賴，便於測試替換。
     * 【概念】相較欄位 {@code @Autowired}，建構子注入能保證物件建立時依賴已就緒。
     */
    @Autowired
    public OrderProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 【職責】非同步發送訂單事件至固定 topic。
     * 【技巧】呼叫 {@code kafkaTemplate.send}，由 Spring Kafka 處理序列化與連線。
     * 【概念】fire-and-forget 適合示範；正式環境通常需回呼、重試或與交易邊界協調。
     * @param order 已持久化（或更新後）的訂單本體
     */
    public void sendOrderEvent(Order order) {
        logger.info(String.format("#### -> Producing message -> %s", order));
        this.kafkaTemplate.send(TOPIC, order);
    }
}
