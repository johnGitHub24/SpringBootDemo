package com.demo.springbootdemo.library.controller;

import com.demo.springbootdemo.library.dto.BookCreateRequest;
import com.demo.springbootdemo.library.dto.BookDto;
import com.demo.springbootdemo.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 【職責】提供圖書查詢、維護與借還操作的 REST API，將請求轉交 {@link BookService}。
 * <p>【技巧】結合 Spring MVC 路由、{@code @Valid} 驗證、{@link Pageable} 分頁與 {@link ResponseEntity} 回應組裝。
 * <p>【概念】Controller 只處理 HTTP 轉接；以 Service 集中規則可避免 REST、gRPC 等多個入口出現不同的借還邏輯。
 * <p>【邊界】不直接存取 Repository 或 DAO，也不自行決定圖書狀態轉換。
 */
@RestController
@RequestMapping("/library")
public class BookController {

    @Autowired
    private BookService bookService;

    @Value("${library.name}")
    private String libraryName;

    @Value("${library.version}")
    private String libraryVersion;

    @Value("${library.welcome-message}")
    private String welcomeMessage;

    /**
     * 【職責】回傳圖書館展示組態。
     * <p>【技巧】以 {@code @Value} 將 properties 值注入欄位後再組裝回應。
     * <p>【概念】組態與程式碼分離，能讓同一套程式在不同環境替換名稱或版本而無須重新編譯。
     * @return 200 與組態組出的說明字串
     */
    @GetMapping("/info")
    public ResponseEntity<String> getLibraryInfo() {
        String info = String.format("%s (版本: %s) - %s", libraryName, libraryVersion, welcomeMessage);
        return ResponseEntity.ok(info);
    }

    /**
     * 【職責】分頁回傳圖書列表。
     * <p>【技巧】以 Spring Data Web 的 {@link Pageable} 與 {@code @PageableDefault} 將 query string 轉成分頁條件。
     * <p>【概念】分頁避免一次載入整張表；預設值提供安全上限，使用者仍可在允許範圍調整排序與頁碼。
     * @param pageable 分頁與排序條件（可由 query string 覆寫）
     * @return 200 與 {@link BookDto} 分頁結果
     */
    @GetMapping("/books")
    public ResponseEntity<Page<BookDto>> getBooks(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooks(pageable));
    }

    /**
     * 【職責】依主鍵取得單本圖書。
     * <p>【技巧】直接委派 Service，讓 {@code @ControllerAdvice} 統一轉譯找不到的例外。
     * <p>【概念】集中式例外處理可讓每個端點維持成功流程，並確保錯誤 JSON 格式一致。
     * @param id 圖書主鍵
     * @return 200 與圖書 DTO；不存在時由全域例外處理回 404
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * 【職責】建立一筆新圖書。
     * <p>【技巧】以 {@code @RequestBody @Valid} 觸發 Bean Validation，成功後用 {@code 201 Created} 表示建立結果。
     * <p>【概念】先在 API 邊界驗證輸入，可將格式錯誤與後續業務規則錯誤清楚區分。
     * @param request 新增內容（書名、作者等）
     * @return 201 與建立後的圖書 DTO
     */
    @PostMapping("/books")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    /**
     * 依主鍵更新圖書基本資料；請求體須通過 Bean Validation。
     *
     * @param id      目標圖書主鍵
     * @param request 欲覆寫的欄位
     * @return 200 與更新後的圖書 DTO；不存在時回 404
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Integer id, 
            @RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    /**
     * 依主鍵刪除圖書。
     *
     * @param id 目標圖書主鍵
     * @return 204 無內容
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 借出指定圖書給借閱人。
     *
     * @param id       圖書主鍵
     * @param borrower 借閱人姓名（query 參數）
     * @return 200 與更新後的圖書 DTO；已借出或不存在時由 Service／例外處理回應錯誤
     */
    @PostMapping("/books/{id}/borrow")
    public ResponseEntity<BookDto> borrowBook(
            @PathVariable Integer id, 
            @RequestParam String borrower) {
        return ResponseEntity.ok(bookService.borrowBook(id, borrower));
    }

    /**
     * 歸還指定圖書，清除借閱狀態。
     *
     * @param id 圖書主鍵
     * @return 200 與更新後的圖書 DTO；不存在時回 404
     */
    @PostMapping("/books/{id}/return")
    public ResponseEntity<BookDto> returnBook(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.returnBook(id));
    }
}
