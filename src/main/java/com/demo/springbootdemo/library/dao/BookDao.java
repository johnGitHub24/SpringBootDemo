package com.demo.springbootdemo.library.dao;

import com.demo.springbootdemo.library.model.Book;

import java.util.List;

/**
 * 【職責】定義圖書 JDBC 持久化存取契約。
 * <p>【技巧】以介面描述查詢／寫入操作，實作可用 NamedParameterJdbcTemplate。
 * <p>【概念】DAO 與 Repository 都是資料存取抽象；本介面示範手寫 JDBC 路徑，現行主流程多走 {@link BookRepository}。
 * <p>【邊界】不含商業規則（借還可否由 Service 決定）。
 */
public interface BookDao {

    /** 【職責】查詢全部圖書（無分頁）。【技巧】回傳實體列表。【概念】教學用全表查詢；正式環境應分頁。 */
    List<Book> getBooks();

    /** 【職責】依主鍵查詢單本圖書。【技巧】不存在時回 null。【概念】與拋例外風格不同，呼叫端需自行判空。 */
    Book getBookById(Integer id);

    /** 【職責】新增圖書並取得產生的主鍵。【技巧】由實作處理 KeyHolder。【概念】主鍵由資料庫產生，呼叫端不應自訂。 */
    Integer createBook(Book book);

    /** 【職責】更新書名、作者、分類。【技巧】不變更借閱狀態。【概念】基本資料與借閱狀態分離更新，降低誤改風險。 */
    void updateBook(Integer id, Book book);

    /** 【職責】依主鍵刪除圖書列。【技巧】直接刪除對應列。【概念】刪除語意由呼叫端保證存在性。 */
    void deleteBook(Integer id);

    /** 【職責】更新借閱旗標與借閱人。【技巧】供借出／歸還流程呼叫。【概念】把狀態欄位更新集中，避免散落多段 SQL。 */
    void updateBorrowStatus(Integer id, Boolean isBorrowed, String borrowerName);
}
