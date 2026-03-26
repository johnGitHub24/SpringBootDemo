package com.demo.springbootdemo.advanced.ms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 模擬「訂單微服務」
 */
@RestController
@RequestMapping("/api/ms/orders")
public class SimulatedOrderController {

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
