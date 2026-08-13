package com.demo.springbootdemo.advanced.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 【職責】單元驗證 {@link KafkaProducerService#sendMessage} 會委派 {@link KafkaTemplate}。
 * 【技巧】Mock template 回未完成的 Future，避免 {@code whenComplete} 碰 null metadata。
 * 【概念】與 {@code KafkaBrokerIntegrationTest} 共用 CASE-KAFKA-INT-001。
 */
@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceUnitTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService producerService;

    /**
     * CASE-KAFKA-INT-001：發送 payment-topic 訊息不拋錯。
     * Given: Mock KafkaTemplate；When: sendMessage；Then: 不拋例外且 send 被呼叫。
     */
    @Test
    void sendMessage_doesNotThrow() {
        given(kafkaTemplate.send(eq("payment-topic"), eq("test-key"), eq("Test Payment Message")))
                .willReturn(new CompletableFuture<SendResult<String, String>>());

        assertDoesNotThrow(() ->
                producerService.sendMessage("payment-topic", "test-key", "Test Payment Message"));

        verify(kafkaTemplate).send("payment-topic", "test-key", "Test Payment Message");
    }
}
