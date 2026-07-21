package com.demo.controller;

import com.demo.model.Order;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【職責】提供訂單資源的 REST 入口，將 HTTP 請求轉交 {@link com.demo.service.OrderService}。
 * 【技巧】以 Spring MVC 的路由註解綁定路徑與請求內容，並用 {@link ResponseEntity} 表達查無資料的 HTTP 回應。
 * 【概念】Controller 是傳輸層轉接器；把規則留在 Service，可讓相同業務從 REST、排程或訊息消費者重複使用。
 * 【邊界】不直接操作 Repository、不決定快取或 Kafka 事件，也不承載訂單商業規則。
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 注入訂單服務。
     *
     * @param orderService 訂單商業邏輯服務
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 【職責】回傳目前可查詢的全部訂單。
     * 【技巧】將集合回傳委派給 Service，讓 Spring MVC 依內容協商序列化為 JSON。
     * 【概念】薄 Controller 不自行查資料，可避免 HTTP 協定細節與持久化邏輯耦合。
     * @return 訂單清單，無資料時為空集合
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * 【職責】依主鍵取得單筆訂單，缺少資料時回傳 404。
     * 【技巧】以 {@code Optional.map} 將 Service 的查詢結果映射成 {@link ResponseEntity}。
     * 【概念】Optional 把「可能不存在」放進型別契約，避免以 {@code null} 控制 HTTP 分支。
     * @param id 訂單主鍵
     * @return 200 與訂單本體，或 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 【職責】接收建立訂單請求並交由 Service 持久化。
     * 【技巧】使用 {@code @RequestBody} 讓 Jackson 將 JSON 綁定為 {@link com.demo.dto.OrderRequest}。
     * 【概念】請求 DTO 將 API 輸入與 JPA Entity 分開，避免客戶端直接控制持久化模型。
     * @param request 建立所需欄位（客戶、商品、數量、單價等）
     * @return 持久化後的訂單（含產生的 id）
     */
    @PostMapping
    public Order createOrder(@RequestBody com.demo.dto.OrderRequest request) {
        return orderService.createOrder(request);
    }

    /**
     * 【職責】將指定訂單的更新請求交給 Service，並轉譯查無資料結果。
     * 【技巧】以 {@code try/catch} 把現有 Service 的 {@link RuntimeException} 契約映射成 404 回應。
     * 【概念】HTTP 狀態碼屬傳輸層責任；Service 不須依賴 Web 型別即可被其他入口重用。
     * @param id      訂單主鍵
     * @param request 欲更新的欄位
     * @return 200 與更新後訂單，或 404
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
     * 【職責】委派 Service 刪除指定訂單。
     * 【技巧】以 {@code @DeleteMapping} 對應 REST 刪除語意，回傳既有 API 約定的空 200 回應。
     * 【概念】Controller 只描述 HTTP 邊界；實際刪除策略與資料一致性屬 Service／Repository 的責任。
     * @param id 訂單主鍵
     * @return 200（無內容本體）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
