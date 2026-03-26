package com.demo.kafka;

import com.demo.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void consume(Order order) {
        logger.info(String.format("#### -> Consumed message -> %s", order));
        // 在這裡實作後續業務邏輯，例如：扣除庫存、發送通知等
    }
}
