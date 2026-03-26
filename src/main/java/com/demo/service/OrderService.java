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
 * 訂單服務層 (Service Layer)
 * 
 * 本類別處理核心業務邏輯，並整合了 Spring 的多項進階功能：
 * 1. 宣告式事務管理 (@Transactional)
 * 2. 緩存機制 (@Cacheable, @CacheEvict)
 * 3. 訊息佇列整合 (Kafka)
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
    }

    /**
     * 取得所有訂單
     * @return 訂單列表
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * 根據 ID 取得訂單 (使用 Redis 快取)
     * 
     * @Cacheable 原理：
     * 當調用此方法時，Spring 會先檢查 Redis 中是否存在 key 為 "orders::id" 的資料。
     * - 若有：直接回傳快取內容，不執行方法本體（不查資料庫）。
     * - 若無：執行方法（查資料庫），並將結果寫入 Redis 以供下次使用。
     * 
     * @param id 訂單 ID
     * @return 包含訂單的 Optional
     */
    @Cacheable(value = "orders", key = "#id")
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 建立新訂單 (並發送 Kafka 事件)
     * 
     * @Transactional 原理：
     * 確保方法內的資料庫操作（orderRepository.save）符合 ACID 原則。
     * 若方法執行過程中拋出 RuntimeException，Spring AOP 會捕捉並觸發 Rollback，回滾已執行的 SQL。
     * 
     * @param request 訂單請求 DTO
     * @return 建立後的訂單
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
     * 更新訂單 (清除快取並發送事件)
     * 
     * @CacheEvict 原理：
     * 當資料更新時，必須「主動失效」舊的快取，以確保資料一致性 (Cache-Aside Pattern)。
     * 執行此方法後，Redis 中對應的 key 將會被刪除。
     * 
     * @param id 訂單 ID
     * @param request 更新的詳細資料
     * @return 更新後的訂單
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
     * 刪除訂單 (清除快取)
     * @param id 訂單 ID
     */
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
