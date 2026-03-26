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
 * Kafka 生產者整合測試
 * 
 * 測試重點：
 * 1. 確保訊息能成功發送至 EmbeddedKafka。
 * 2. 驗證非同步 Callback 邏輯。
 * 
 * 註：使用 properties 排除 Seata 以免干擾測試環境
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
     * 測試 Kafka 訊息發送與接收
     * 1. 使用 ProducerService 發送訊息
     * 2. 利用測試內部的 @KafkaListener 監聽並驗證訊息內容
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
     * 內部監聽器：專門用於測試驗證
     */
    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(ConsumerRecord<String, String> record) {
        records.add(record.value());
    }
}
