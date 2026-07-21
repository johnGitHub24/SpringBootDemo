package com.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 【職責】承載訂單建立／更新 API 的輸入欄位。
 * 【技巧】以 Lombok {@code @Data} 產生存取子，作為 Jackson 反序列化目標。
 * 【概念】請求 DTO 將外部輸入與 JPA Entity 隔離，避免客戶端直接控制持久化模型與關聯。
 * 【邊界】不含持久化、商業規則執行；驗證與組裝由 Controller／Service 處理。
 */
@Data
public class OrderRequest {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String customerName;
    private String status;
}
