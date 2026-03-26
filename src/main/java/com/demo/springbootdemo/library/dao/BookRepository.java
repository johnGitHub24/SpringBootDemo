package com.demo.springbootdemo.library.dao;

import com.demo.springbootdemo.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 圖書儲存庫介面 (Spring Data JPA Repository)
 * 升級內容：繼承 JpaRepository 自動獲得所有 CRUD 與分頁功能
 * 取代原有的 BookDao 與 BookDaoImpl
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // 這裡不需要寫任何 SQL！JpaRepository 已經實作了基本的增刪改查。
}
