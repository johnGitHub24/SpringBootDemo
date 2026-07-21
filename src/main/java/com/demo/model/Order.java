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
 * 【職責】映射訂單資料表 {@code orders}，承載客戶、商品摘要、金額、狀態與明細關聯。
 * 【技巧】以 JPA／Hibernate 註解定義主鍵、列舉字串、建立時間戳，並以 {@code @OneToMany} 級聯明細。
 * 【概念】實體描述持久化結構；商業規則應留在 Service，API 形狀可用 DTO 隔離，避免 Entity 外洩造成耦合。
 * 【邊界】不含商業規則驗證、HTTP 組裝或 Kafka 事件發送。
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
     * 【職責】維護訂單與明細的一對多關聯集合。
     * 【技巧】{@code mappedBy} 表示外鍵在明細端；{@code cascade=ALL} 與 {@code orphanRemoval} 讓主檔儲存／移除同步明細。
     * 【概念】雙向關聯需兩邊一致；新增明細應走 {@link #addItem}，避免只改一邊造成 ORM 狀態不一致。
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @lombok.ToString.Exclude
    private List<OrderItem> items = new ArrayList<>();

    /**
     * 【職責】以客戶名稱建立待處理訂單骨架。
     * 【技巧】建構時固定初始 {@link OrderStatus#PENDING}，其餘必填欄位由呼叫端補齊後再儲存。
     * 【概念】建構子表達「最小合法起點」；把預設狀態集中於此可避免各處手寫漏設。
     * @param customerName 客戶名稱
     */
    public Order(String customerName) {
        this.customerName = customerName;
        this.status = OrderStatus.PENDING;
        // set default or null for other fields if needed, 
        // strictly speaking productName etc are @Column(nullable=false) so they should be set before saving
    }

    /**
     * 【職責】將明細加入本訂單並同步雙向關聯。
     * 【技巧】同時更新集合與 {@link OrderItem#setOrder}，維持 JPA 雙向一致性。
     * 【概念】只改一邊會讓記憶體物件圖與資料庫外鍵語意脫節，除錯時特別難查。
     * @param item 訂單明細
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
