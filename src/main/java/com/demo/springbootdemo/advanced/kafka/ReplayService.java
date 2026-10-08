package com.demo.springbootdemo.advanced.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

/**
 * 【職責】依 listenerId 停止／重啟 Kafka Listener 容器，示範訊息重放（Replay）思路。
 * <p>【技巧】透過 {@link KafkaListenerEndpointRegistry} 查找並控制動態 Listener 容器。
 * <p>【概念】重放常用於錯誤恢復或對帳；正式環境需真實 seek／冪等，否則會重複副作用。
 * <p>【邊界】不實作實際 {@code seek}，也不負責跨叢集運維。
 */
@Slf4j
@Service
public class ReplayService {

    private final KafkaListenerEndpointRegistry registry;

    /**
     * @param registry Kafka 監聽器端點登錄表，用來查找並控制動態 Listener 容器
     */
    public ReplayService(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    /**
     * 對指定 Listener 執行「停容器 →（模擬）重置 offset → 重啟」的重放流程。
     * <br>找不到對應容器時僅記錄警告並結束，不拋出例外。
     *
     * @param listenerId 對應 {@code @KafkaListener} 的 {@code id} 屬性
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
