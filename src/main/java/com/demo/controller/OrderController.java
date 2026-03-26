package com.demo.controller;

import com.demo.model.Order;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 訂單控制器 (Controller Layer)
 * 
 * 本類別負責處理外部 HTTP 請求，並遵循 RESTful API 設計原則：
 * 1. 使用 HTTP 方法表示動作 (GET, POST, PUT, DELETE)。
 * 2. 使用 URL 路徑表示資源 (/api/orders)。
 * 
 * Spring MVC 運作機制：
 * - DispatcherServlet 接收請求後，根據 @RequestMapping 尋找對應的 Controller 方法。
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 取得所有訂單 (HTTP GET)
     * 使用場景：列表顯示。
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * 根據 ID 取得訂單 (HTTP GET with Path Variable)
     * @PathVariable：將 URL 中的 {id} 綁定到方法參數上。
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 建立新訂單 (HTTP POST)
     * @RequestBody：將 HTTP Request Body 中的 JSON 自動映射（反序列化）為 Java 物件。
     */
    @PostMapping
    public Order createOrder(@RequestBody com.demo.dto.OrderRequest request) {
        return orderService.createOrder(request);
    }

    /**
     * 更新訂單 (HTTP PUT)
     * REST 慣例：PUT 用於全面更新，PATCH 用於部分更新。此處使用 PUT 簡化實作。
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody com.demo.dto.OrderRequest request) {
        try {
            Order updatedOrder = orderService.updateOrder(id, request);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 刪除訂單
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
