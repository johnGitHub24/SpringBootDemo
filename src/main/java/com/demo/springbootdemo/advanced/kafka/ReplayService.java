package com.demo.springbootdemo.advanced.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

/**
 * Kafka 訊息重放服務 (Message Replay Service)
 * 
 * 功能描述：
 * 展示如何透過手動控制 Kafka Listener 容器，實現訊息重放的設計思路。
 * 這在區塊鏈交易校對或系統錯誤恢復時非常有用。
 */
@Slf4j
@Service
public class ReplayService {

    private final KafkaListenerEndpointRegistry registry;

    /**
     * @param registry Kafka 監聽器註冊表，用於管理所有動態監聽容器
     */
    public ReplayService(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    /**
     * 重放特定監聽器的訊息
     * 
     * 實務邏輯：
     * 1. 停止 Consumer (container.stop())
     * 2. 重置 Offset (通常透過 KafkaConsumer.seek() 實現)
     * 3. 重啟 Consumer (container.start())
     * 
     * @param listenerId 監聽器的唯一代號 (即 @KafkaListener 的 id 屬性)
     */
    public void replayMessages(String listenerId) {
        log.info("觸發訊息重放流程 - 準備重新讀取歷史數據, ListenerID: {}", listenerId);
        
        // 取得對應的監聽器容器
        MessageListenerContainer container = registry.getListenerContainer(listenerId);
        
        if (container != null) {
            log.info("步驟 1: 正在停止對應的 Consumer 執行緒...");
            container.stop();
            
            // 邏輯說明：
            // 在此處通常會進行持久化狀態清除，
            // 或是調用底層的 consumer.seekToBeginning()。
            log.info("步驟 2: 正在將 Offset 重設為初始位置 (Earliest)... [模擬操作]");
            
            log.info("步驟 3: 重啟 Consumer，系統將從歷史數據開始重新消費...");
            container.start();
        } else {
            log.warn("找不到指定的 ListenerID: {}，重放操作終止。", listenerId);
        }
    }
}
