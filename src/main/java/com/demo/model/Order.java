package com.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 訂單實體 (Entity Layer / Data Layer)
 * 
 * 本類別映射至資料庫中的 orders 表。
 * @Entity: 標記此類別為 JPA 實體。
 * @Table: 指定對應的資料表名稱。
 * 
 * 繼承 Serializable 介面是為了讓此物件可以被序列化並儲存進 Redis 緩存。
 */
@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order implements Serializable {

    @Id // 標記此欄位為 Primary Key (主鍵)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 由資料庫自動生成遞增 ID
    private Long id;

    private String customerName;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @CreationTimestamp // 由 Hibernate 自動在儲存時填入目前時間
    @Column(updatable = false) // 限制此欄位在建立後不可被更新
    private LocalDateTime createdAt;
    
    /**
     * 一對多關聯映射 (@OneToMany)
     * - mappedBy: 指向 OrderItem 類別中的 "order" 屬性，表示 OrderItem 是關係維護端。
     * - cascade: 級聯操作。當儲存 Order 時，其底下的 OrderItem 也會自動儲存 (ALL)。
     * - orphanRemoval: 如果從清單中移除 OrderItem，資料庫中對應的紀錄也會被刪除。
     * - fetch: 加載策略。LAZY 表示只有在真正存取到 items 時才會執行 SQL 查詢。
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @lombok.ToString.Exclude
    private List<OrderItem> items = new ArrayList<>();

    public Order(String customerName) {
        this.customerName = customerName;
        this.status = OrderStatus.PENDING;
        // set default or null for other fields if needed, 
        // strictly speaking productName etc are @Column(nullable=false) so they should be set before saving
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
