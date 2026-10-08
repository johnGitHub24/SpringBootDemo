package com.demo.service;

import com.demo.model.Order;
import com.demo.model.OrderItem;
import com.demo.model.OrderStatus;
import com.demo.repository.OrderRepository;
import com.demo.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.demo.kafka.OrderProducer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 【職責】執行訂單建立、查詢、更新與刪除，並協調持久化、快取和 Kafka 事件。
 * <p>【技巧】結合 Spring {@code @Transactional}、{@code @Cacheable}／{@code @CacheEvict} 與建構子注入管理跨元件協作。
 * <p>【概念】Service 是業務規則的邊界：Controller、排程或訊息入口皆可呼叫它，而不需要知道 HTTP 細節。
 * <p>【邊界】不組裝 HTTP 狀態碼、不直接使用 Request／Response，也不實作 Repository 的資料庫存取細節。
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    /**
     * 注入持久化與事件發送依賴。
     *
     * @param orderRepository 訂單 Repository
     * @param orderProducer   訂單事件 Producer
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
    }

    /**
     * 【職責】取得目前所有訂單。
     * <p>【技巧】直接委派 Spring Data 的 {@code findAll}。
     * <p>【概念】此教學 API 未分頁；實際大量資料應以分頁查詢限制單次記憶體與回應大小。
     * @return 訂單清單
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * 【職責】依主鍵查詢訂單，並快取成功的查詢結果。
     * <p>【技巧】{@code @Cacheable} 以 SpEL 的 {@code #id} 作為快取鍵。
     * <p>【概念】快取可減少重複讀取，但寫入時必須同步失效，否則讀者會看到過期資料。
     * @param id 訂單主鍵
     * @return 有則回傳訂單，否則 empty
     */
    @Cacheable(value = "orders", key = "#id")
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 【職責】由請求建立訂單主檔與首筆明細，持久化後嘗試發出訂單事件。
     * <p>【技巧】透過 {@code @Transactional} 包住 JPA 寫入，並以雙向關聯輔助方法建立主檔與明細關係。
     * <p>【概念】資料庫交易與訊息發送不是天然同一個原子操作；此範例刻意記錄 Kafka 失敗而不回滾，正式系統可用 Outbox。
     * @param request 建立所需欄位
     * @return 已儲存的訂單
     */
    @Transactional
    public Order createOrder(com.demo.dto.OrderRequest request) {
        // 1. 建立訂單主檔
        Order order = new Order(request.getCustomerName() != null ? request.getCustomerName() : "Guest");
        
        // 設定必填欄位 (因為 Entity 中有 @Column(nullable = false))
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        // 2. 建立訂單項目
        if (request.getProductName() != null) {
            OrderItem item = new OrderItem(
                request.getProductName(), 
                request.getQuantity(), 
                request.getPrice()
            );
            order.addItem(item);
        }

        Order savedOrder = orderRepository.save(order);
        
        // 發送 Kafka 事件
        // 注意: 若 Kafka 未啟動，這裡可能會報錯。在生產環境應有錯誤處理或非同步機制。
        try {
            orderProducer.sendOrderEvent(savedOrder);
        } catch (Exception e) {
            // Log error but don't fail the transaction for demo purpose
            System.err.println("Kafka send failed: " + e.getMessage());
        }
        
        return savedOrder;
    }

    /**
     * 【職責】更新既有訂單的可變欄位、失效快取，並嘗試通知下游。
     * <p>【技巧】以 Repository 的 {@code Optional.map} 在找到實體後套用變更，並由 {@code @CacheEvict} 清除舊快取。
     * <p>【概念】先失效再讀取可避免舊快取持續被使用；狀態字串轉 enum 時則需明確處理非法輸入。
     * @param id      訂單主鍵
     * @param request 欲套用的欄位（null 表示不變更該欄）
     * @return 更新後的訂單
     * @throws RuntimeException 訂單不存在時
     */
    @CacheEvict(value = "orders", key = "#id")
    @Transactional
    public Order updateOrder(Long id, OrderRequest request) {
        return orderRepository.findById(id)
                .map(order -> {
                    if (request.getCustomerName() != null) {
                        order.setCustomerName(request.getCustomerName());
                    }
                    
                    // 簡化邏輯：若有商品資訊，則清空舊項目並加入新項目
                    if (request.getProductName() != null) {
                        order.getItems().clear();
                        OrderItem item = new OrderItem(
                            request.getProductName(), 
                            request.getQuantity(), 
                            request.getPrice()
                        );
                        order.addItem(item);
                    }

                    if (request.getStatus() != null) {
                        try {
                            order.setStatus(com.demo.model.OrderStatus.valueOf(request.getStatus().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            // 忽略不合法的狀態或記錄日誌
                        }
                    }
                    
                    Order updatedOrder = orderRepository.save(order);
                    
                    // 發送 Kafka 事件
                    try {
                        orderProducer.sendOrderEvent(updatedOrder);
                    } catch (Exception e) {
                        System.err.println("Kafka send failed during update: " + e.getMessage());
                    }
                    
                    return updatedOrder;
                })
                .orElseThrow(() -> new RuntimeException("找不到訂單 ID: " + id));
    }

    /**
     * 【職責】移除指定訂單並使對應快取失效。
     * <p>【技巧】以 {@code @CacheEvict} 將快取生命週期綁定在寫入方法上。
     * <p>【概念】刪除資料卻保留快取會產生幽靈資料，因此快取失效是寫入流程的一部分。
     * @param id 訂單主鍵
     */
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
