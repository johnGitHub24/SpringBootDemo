package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 【職責】映射訂單明細表 {@code order_items}，描述單一商品列的名稱、數量與單價。
 * 【技巧】以 {@code @ManyToOne} 延遲載入所屬 {@link Order}，並用 {@code @JsonBackReference} 避免序列化循環。
 * 【概念】明細是訂單的組成部分；級聯與孤兒刪除由主檔端控制，明細本身不應獨立承載訂單生命週期規則。
 * 【邊界】不含庫存扣減或價格策略。
 */
@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private Integer quantity;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    @lombok.ToString.Exclude
    private Order order;

    /**
     * 【職責】以商品名稱、數量與單價建立尚未掛上訂單的明細。
     * 【技巧】建構子只填業務欄位，關聯訂單稍後由 {@link Order#addItem} 設定。
     * 【概念】先建值物件再掛關聯，可讓組裝流程在 Service 中更清楚。
     * @param productName 商品名稱
     * @param quantity 數量
     * @param price 單價
     */
    public OrderItem(String productName, Integer quantity, BigDecimal price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * 【職責】計算本列小計（單價 × 數量）。
     * 【技巧】使用 {@link BigDecimal#multiply} 與 {@code valueOf(quantity)} 避免浮點誤差。
     * 【概念】金額計算應使用 BigDecimal；把小計放在明細可讓彙總邏輯重用同一公式。
     * @return 小計金額
     */
    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
