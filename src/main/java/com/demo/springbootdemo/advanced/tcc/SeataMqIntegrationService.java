package com.demo.springbootdemo.advanced.tcc;

import com.demo.springbootdemo.advanced.kafka.KafkaProducerService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【職責】示範 Seata 全域事務與 Kafka 通知的協作流程。
 * 【技巧】在 {@code @GlobalTransactional} 內執行 TCC Try，成功路徑再發送軌跡訊息。
 * 【概念】分散式事務與訊息發送的時序會影響一致性；實務常把 MQ 放 Confirm 後或改用事務訊息。
 * 【邊界】不實作 Confirm／Cancel 本體（由 Seata 調度 {@link TccAction}）。
 */
@Slf4j
@Service
public class SeataMqIntegrationService {

    private final TccAction tccAction;
    private final KafkaProducerService kafkaProducerService;

    /**
     * @param tccAction             TCC 資源動作，負責 Try／Confirm／Cancel
     * @param kafkaProducerService  用於發送事務軌跡的 Kafka 生產者
     */
    public SeataMqIntegrationService(TccAction tccAction, KafkaProducerService kafkaProducerService) {
        this.tccAction = tccAction;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * 執行示範用支付全域事務：先 TCC 預留資源，再發送 Kafka 軌跡；預留失敗則拋出例外觸發回滾。
     *
     * @param orderId 訂單編號
     * @param amount  支付金額
     * @throws RuntimeException 當 TCC 預留失敗時拋出，以觸發 Seata 全域回滾
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
