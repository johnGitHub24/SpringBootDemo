package com.demo.springbootdemo.library.dao;

import com.demo.springbootdemo.library.model.Book;

import java.util.List;

/**
 * 圖書資料存取介面 (Data Access Object)
 * 示範 Day 24-27: 使用 Spring JDBC 操作資料庫
 */
public interface BookDao {
    
    // 取得所有圖書列表
    List<Book> getBooks();

    // 根據 ID 查詢特定圖書
    Book getBookById(Integer id);

    // 新增圖書，回傳產生的自動增量 ID
    Integer createBook(Book book);

    // 更新圖書資訊
    void updateBook(Integer id, Book book);

    // 刪除圖書
    void deleteBook(Integer id);

    /**
     * 更新圖書的借閱狀態
     * @param id 圖書 ID
     * @param isBorrowed 是否借出
     * @param borrowerName 借閱者姓名 (若歸還則為 null)
     */
    void updateBorrowStatus(Integer id, Boolean isBorrowed, String borrowerName);
}
