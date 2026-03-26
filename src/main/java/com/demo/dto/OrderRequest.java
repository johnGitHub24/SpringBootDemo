package com.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderRequest {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String customerName;
    private String status;
}
