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
 * 進階圖書控制器 (現代化架構)
 * 升級內容：
 * 1. 使用 @Valid 進行參數校驗
 * 2. 實作 Pageable 分頁查詢 (預設一頁 5 筆)
 * 3. 透過 DTO 進行資料交換
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
     * 獲取圖書館資訊 (示範 @Value)
     */
    @GetMapping("/info")
    public ResponseEntity<String> getLibraryInfo() {
        String info = String.format("%s (版本: %s) - %s", libraryName, libraryVersion, welcomeMessage);
        return ResponseEntity.ok(info);
    }

    /**
     * 查詢圖書列表 (分頁版)
     * URL 範例: /library/books?page=0&size=2&sort=id,desc
     */
    @GetMapping("/books")
    public ResponseEntity<Page<BookDto>> getBooks(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooks(pageable));
    }

    /**
     * 根據 ID 查詢特定圖書
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * 新增圖書 (加上 @Valid)
     */
    @PostMapping("/books")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    /**
     * 更新圖書 (加上 @Valid)
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Integer id, 
            @RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    /**
     * 刪除圖書
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 借閱圖書
     */
    @PostMapping("/books/{id}/borrow")
    public ResponseEntity<BookDto> borrowBook(
            @PathVariable Integer id, 
            @RequestParam String borrower) {
        return ResponseEntity.ok(bookService.borrowBook(id, borrower));
    }

    /**
     * 歸還圖書
     */
    @PostMapping("/books/{id}/return")
    public ResponseEntity<BookDto> returnBook(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.returnBook(id));
    }
}
