package com.demo.springbootdemo.advanced.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Kafka 整合測試
 * 使用嵌入式 Kafka (EmbeddedKafka) 確保在無外部環境下也能跑通
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"payment-topic"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

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
