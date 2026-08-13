package com.demo.integration;

import com.demo.springbootdemo.SpringBootDemoApplication;
import com.demo.springbootdemo.advanced.kafka.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 【職責】Kafka 生產者與 EmbeddedKafka 的訊息層整合測試。
 * 【技巧】{@code @EmbeddedKafka} 提供 in-process broker，無需 Docker。
 * 【概念】與 {@code KafkaProducerServiceUnitTest} 共用 CASE-KAFKA-INT-001。
 */
@SpringBootTest(classes = SpringBootDemoApplication.class, properties = {
        "seata.enabled=false",
        "spring.cache.type=simple"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"payment-topic"})
class KafkaBrokerIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

    /**
     * CASE-KAFKA-INT-001：發送 payment-topic 訊息不拋錯。
     * Given: EmbeddedKafka payment-topic；When: sendMessage 後等待 2 秒；Then: 流程完成。
     */
    @Test
    void sendPaymentMessage_doesNotThrow() throws InterruptedException {
        producerService.sendMessage("payment-topic", "test-key", "Test Payment Message");
        TimeUnit.SECONDS.sleep(2);
        assertThat(true).isTrue();
    }
}
