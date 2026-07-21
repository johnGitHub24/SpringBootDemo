package com.demo.springbootdemo.advanced.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 【職責】以 {@link KafkaTemplate} 非同步發送業務事件至指定 Topic。
 * 【技巧】以 key 決定分區以利同 key 保序，並以 {@link CompletableFuture} 回調記錄成功／失敗。
 * 【概念】Producer 與主流程解耦可削峰；正式環境需補重試、事務訊息或死信策略。
 * 【邊界】不負責消費者處理。
 */
@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * @param kafkaTemplate Spring 提供的 Kafka 操作範本（底層含連線池與非同步發送）
     */
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 非阻塞發送字串訊息；以 key 決定分區以利同 key 保序，結果於回調中記錄。
     *
     * @param topic   目標 Topic
     * @param key     訊息 Key（影響分區與同 key 順序）
     * @param message 訊息內容（Payload）
     */
    public void sendMessage(String topic, String key, String message) {
        log.info("發送訊息至 Kafka - Topic: {}, Key: {}, Payload: {}", topic, key, message);
        
        // send() 會立即返回，不會等待伺服器回應 (非阻塞)
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
        
        // 註冊回調函式，處理非同步發送結果
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                // 發送成功：記錄 Offset 與 Partition 資訊
                log.info("訊息發送成功: Topic={}, Partition={}, Offset={}", 
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                // 發送失敗：進行錯誤處理 (例如：寫入資料庫備份或重試)
                log.error("訊息發送失敗: Topic={}, Error={}", topic, ex.getMessage());
            }
        });
    }

    /**
     * 將訊息送至指定分區，用於示範高併發下需固定分區或手動負載分配的場景。
     *
     * @param topic     目標 Topic
     * @param partition 目標分區編號
     * @param key       訊息 Key
     * @param message   訊息內容
     */
    public void sendToPartition(String topic, Integer partition, String key, String message) {
        log.info("發送訊息至特定分區 - Topic: {}, Partition: {}, Message: {}", topic, partition, message);
        kafkaTemplate.send(topic, partition, key, message);
    }
}
