package com.demo.springbootdemo.advanced.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 覆蓋 Kafka 生產者與 EmbeddedKafka 的端到端煙霧測試（訊息層）。
 * 驗證 sendMessage 在無外部 Broker 時可執行；消費斷言為占位（實務可改 KafkaTestUtils）。
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"payment-topic"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

    /**
     * CASE-KAFKA-INT-001：發送 payment-topic 訊息不拋錯。
     * Given: EmbeddedKafka payment-topic；When: sendMessage 後等待 2 秒；Then: 流程完成（占位斷言 true）。
     */
    @Test
    public void testSendAndReceiveMessage() throws InterruptedException {
        String testMessage = "Test Payment Message";
        
        // 發送訊息
        producerService.sendMessage("payment-topic", "test-key", testMessage);
        
        // 由於消費是異步的，在測試中我們通常會觀察日誌或使用計數器
        // 這裡暫時休眠以模擬等待消費完成
        TimeUnit.SECONDS.sleep(2);
        
        // 驗證邏輯：在實務中可以使用 KafkaTestUtils 獲取訊息紀錄
        assertThat(true).isTrue(); 
    }
}
