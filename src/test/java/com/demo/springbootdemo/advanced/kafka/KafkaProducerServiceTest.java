package com.demo.springbootdemo.advanced.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 覆蓋 {@link KafkaProducerService}（Kafka 生產者／訊息層）的整合測試。
 * 使用 EmbeddedKafka 驗證訊息可送達；內部 {@code @KafkaListener} 僅供測試收取。
 */
@SpringBootTest(properties = {
    "seata.enabled=false"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"test-topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaProducerServiceTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // 用於接收測試訊息的阻塞佇列
    private static final BlockingQueue<String> records = new LinkedBlockingQueue<>();

    /**
     * CASE-KAFKA-PROD-001：發送後可被監聽器收到。
     * Given: EmbeddedKafka topic=test-topic；When: sendMessage；Then: 10 秒內收到相同內容。
     */
    @Test
    public void testSendMessage() throws InterruptedException {
        String testTopic = "test-topic";
        String testMessage = "Hello Kafka Test!";
        
        // 執行發送
        kafkaProducerService.sendMessage(testTopic, "test-key", testMessage);

        // 從佇列中取得訊息 (設定超時避免無限等待)
        String received = records.poll(10, TimeUnit.SECONDS);

        // 驗證結果
        assertThat(received).isEqualTo(testMessage);
    }

    /**
     * 測試用內部監聽器：將收到的訊息放入佇列供斷言。
     */
    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(ConsumerRecord<String, String> record) {
        records.add(record.value());
    }
}
