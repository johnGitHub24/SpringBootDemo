package com.demo.springbootdemo.advanced.tcc;

import com.demo.springbootdemo.advanced.kafka.KafkaProducerService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Seata + MQ 整合範例服務
 * 展示全局事務與訊息隊列的協作模式
 */
@Slf4j
@Service
public class SeataMqIntegrationService {

    private final TccAction tccAction;
    private final KafkaProducerService kafkaProducerService;

    public SeataMqIntegrationService(TccAction tccAction, KafkaProducerService kafkaProducerService) {
        this.tccAction = tccAction;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * 模擬一個包含分散式事務與 MQ 通知的業務流程
     */
    @GlobalTransactional(name = "payment-distributed-transaction", rollbackFor = Exception.class)
    public void executePaymentFlow(String orderId, double amount) {
        log.info(">>> 開始執行全局導航事務 (Global Transaction)");
        
        // 1. 執行 TCC 事務的第一階段 (Try)
        boolean result = tccAction.prepare(null, orderId, amount);
        
        if (!result) {
            throw new RuntimeException("TCC 預留資源失敗，觸發全局回滾");
        }

        // 2. 模擬成功後發送 MQ 通知 (此處使用 Kafka 代表 MQ)
        // 實務中可能在 Confirm 階段完成後再發送，或者利用事務訊息保證一致性
        kafkaProducerService.sendMessage("transaction-log", orderId, "事務執行中... 金額: " + amount);
        
        log.info("<<< 全局事務執行完成，等待 Seata 自動 Confirm");
    }
}
