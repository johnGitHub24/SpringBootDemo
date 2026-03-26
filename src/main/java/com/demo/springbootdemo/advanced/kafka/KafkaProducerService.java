package com.demo.springbootdemo.advanced.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka 訊息發送者服務 (High-Concurrency Producer)
 * 
 * 適用場景：
 * Kafka 訊息發送者服務
 * 
 * 【高併發場景應用】：
 * 1. 日誌非同步蒐集：避免日誌寫入阻塞業務流程。
 * 2. 支付結果回調：多個支付通道併發回傳結果時，透過 Topic 進行削峰填谷。
 * 3. 系統解耦：將即時性要求不高的後續處理（如發送發票、增加點數）丟入 Kafka。
 * 
 * 本服務使用 KafkaTemplate，其底層已實現連線池與非同步發送，支撐高吞吐量。
 */
@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 建構子注入 KafkaTemplate
     * @param kafkaTemplate Spring 提供的 Kafka 操作模板
     */
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 發送簡單字串訊息
     * 
     * @param topic 目標 Topic
     * @param key   訊息 Key (用於分區保序)
     * @param message 訊息內容 (Payload)
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
     * 發送帶有特定分區邏輯的訊息 (展示高併發下的分派)
     */
    public void sendToPartition(String topic, Integer partition, String key, String message) {
        log.info("發送訊息至特定分區 - Topic: {}, Partition: {}, Message: {}", topic, partition, message);
        kafkaTemplate.send(topic, partition, key, message);
    }
}
