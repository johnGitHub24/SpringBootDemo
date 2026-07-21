package com.demo.springbootdemo.library.dao;

import com.demo.springbootdemo.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 【職責】提供圖書實體的 JPA 持久化存取。
 * 【技巧】繼承 {@link JpaRepository} 取得 CRUD 與分頁，供 {@link com.demo.springbootdemo.library.service.BookServiceImpl} 使用。
 * 【概念】Spring Data 以方法命名／繼承介面產生實作，可大幅減少樣板 SQL。
 * 【邊界】不含商業規則。
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // 這裡不需要寫任何 SQL！JpaRepository 已經實作了基本的增刪改查。
}
