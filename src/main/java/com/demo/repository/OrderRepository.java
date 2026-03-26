package com.demo.repository;

import com.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 訂單資料存取介面
 * 繼承 JpaRepository 以獲得基本的 CRUD 功能
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
