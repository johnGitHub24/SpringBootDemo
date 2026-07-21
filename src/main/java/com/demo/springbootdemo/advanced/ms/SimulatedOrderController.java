package com.demo.springbootdemo.advanced.ms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 【職責】模擬「訂單微服務」下游，供 Gateway 教學轉發驗證。
 * 【技巧】薄 {@code @RestController} 依路徑參數回傳固定結構示範資料。
 * 【概念】用同進程模擬下游可先驗證閘道路徑改寫，不必先拆真實多服務部署。
 * 【邊界】不負責真實訂單狀態機、庫存或支付結算。
 */
@RestController
@RequestMapping("/api/ms/orders")
public class SimulatedOrderController {

    /**
     * 【職責】依訂單識別碼回傳示範訂單資料。
     * 【技巧】以 {@code Map.of} 組裝固定欄位 JSON。
     * 【概念】閘道驗證關心路徑與轉發是否正確，回應內容可先用固定假資料。
     */
    @GetMapping("/{id}")
    public Map<String, Object> getOrder(@PathVariable String id) {
        return Map.of(
            "orderId", id,
            "status", "PAID",
            "amount", 999.0,
            "source", "Microservice_Order"
        );
    }
}
