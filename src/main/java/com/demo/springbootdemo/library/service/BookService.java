package com.demo.springbootdemo.library.service;

import com.demo.springbootdemo.library.dto.BookCreateRequest;
import com.demo.springbootdemo.library.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 進階圖書業務邏輯介面
 * 升級內容：加入 DTO 與分頁支援 (Pageable)
 */
public interface BookService {
    
    // 取得所有圖釋列表 (分頁版)
    Page<BookDto> getBooks(Pageable pageable);

    // 取得特定圖書
    BookDto getBookById(Integer id);

    // 新增圖書
    BookDto createBook(BookCreateRequest request);

    // 更新圖書
    BookDto updateBook(Integer id, BookCreateRequest request);

    // 刪除圖書
    void deleteBook(Integer id);

    // 借閱圖書
    BookDto borrowBook(Integer id, String borrowerName);

    // 歸還圖書
    BookDto returnBook(Integer id);
}
