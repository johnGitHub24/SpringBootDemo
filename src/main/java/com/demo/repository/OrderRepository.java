package com.demo.repository;

import com.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 【職責】提供 {@link Order} 的持久化存取。
 * 【技巧】繼承 {@link JpaRepository} 取得標準 CRUD，由 Spring Data 產生實作。
 * 【概念】Repository 只表達資料存取意圖；商業規則與快取策略應留在 Service。
 * 【邊界】不含商業規則。
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
