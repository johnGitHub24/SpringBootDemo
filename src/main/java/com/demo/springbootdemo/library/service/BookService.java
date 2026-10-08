package com.demo.springbootdemo.library.service;

import com.demo.springbootdemo.library.dto.BookCreateRequest;
import com.demo.springbootdemo.library.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 【職責】定義圖書 CRUD 與借還流程的商業邏輯契約。
 * <p>【技巧】以介面隔離實作，對外只交換 DTO／請求物件，隱藏 JPA 實體。
 * <p>【概念】介面讓 Controller 依賴抽象而非具體類別，便於測試替換與多入口（REST／gRPC）共用規則。
 * <p>【邊界】不組裝 HTTP 狀態碼、不直接操作 JDBC／SQL。
 */
public interface BookService {

    /**
     * 【職責】分頁查詢圖書並轉為 DTO。
     * <p>【技巧】接受 Spring Data {@link Pageable}，回傳保留分頁中繼資料的 {@link Page}。
     * <p>【概念】分頁應在資料層執行，避免一次載入整表再切割。
     */
    Page<BookDto> getBooks(Pageable pageable);

    /**
     * 【職責】依主鍵取得單本圖書。
     * <p>【技巧】找不到時拋領域例外，由全域處理器轉 HTTP。
     * <p>【概念】用例外表達「資源不存在」可比回傳 null 更不易被忽略。
     */
    BookDto getBookById(Integer id);

    /**
     * 【職責】依請求內容建立新圖書（預設未借出）。
     * <p>【技巧】輸入為驗證過的 {@link BookCreateRequest}，輸出為對外 DTO。
     * <p>【概念】建立時系統維護借閱狀態，避免客戶端自行指定造成不一致。
     */
    BookDto createBook(BookCreateRequest request);

    /**
     * 【職責】更新既有圖書的書名、作者、分類。
     * <p>【技巧】以 id 定位資源，再套用請求欄位。
     * <p>【概念】更新與建立共用請求形狀可降低 API 表面積，但狀態欄位仍由服務控制。
     */
    BookDto updateBook(Integer id, BookCreateRequest request);

    /**
     * 【職責】刪除指定圖書。
     * <p>【技巧】由實作決定找不到時的語意（通常拋例外或不動作）。
     * <p>【概念】刪除是破壞性操作；授權應在進入服務前由安全層處理（本示範未啟用 Security）。
     */
    void deleteBook(Integer id);

    /**
     * 【職責】將可借圖書標記為已借出並記錄借閱人。
     * <p>【技巧】在交易中檢查狀態後更新旗標與借閱人。
     * <p>【概念】借出是狀態轉移用例；規則集中於服務可避免 Controller 重複判斷。
     */
    BookDto borrowBook(Integer id, String borrowerName);

    /**
     * 【職責】歸還圖書：清除借出旗標與借閱人。
     * <p>【技巧】與借出對稱的狀態更新，回傳最新 DTO。
     * <p>【概念】歸還不刪除紀錄本體，只還原可借狀態，利於後續再借。
     */
    BookDto returnBook(Integer id);
}
