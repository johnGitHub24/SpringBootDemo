package com.demo.springbootdemo.library.service;

import com.demo.springbootdemo.library.dao.BookRepository;
import com.demo.springbootdemo.library.dto.BookDto;
import com.demo.springbootdemo.library.exception.BookNotFoundException;
import com.demo.springbootdemo.library.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 覆蓋 {@link BookServiceImpl}（Service 層）的單元測試。
 * <br>以 Mockito 隔離 {@link BookRepository}，驗證借書商業邏輯，不依賴真實資料庫。
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepository; // 模擬 JPA Repository 層
    
    @InjectMocks
    private BookServiceImpl bookService;

    /**
     * CASE-BOOK-SVC-001：借書成功。
     * <br>Given: 未借出書籍；When: borrowBook；Then: isBorrowed=true、borrowerName 正確，且 save 被呼叫一次。
     */
    @Test
    @DisplayName("測試借書功能：成功路徑")
    public void testBorrowBook_Success() {
        // 1. 準備測試數據
        Integer bookId = 1;
        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("測試用書籍");
        mockBook.setIsBorrowed(false);

        // 2. 定義模擬行為 (When)
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 3. 執行測試 (Action)
        BookDto result = bookService.borrowBook(bookId, "使用者A");

        // 4. 驗證結果 (Assert)
        assertNotNull(result);
        assertTrue(result.getIsBorrowed());
        assertEquals("使用者A", result.getBorrowerName());
        
        // 驗證 Repository 是否真的有被呼叫
        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));
    }

    /**
     * CASE-BOOK-SVC-002：借書失敗（已借出）。
     * <br>Given: 書籍已借出；When: borrowBook；Then: 拋出 BookNotFoundException。
     */
    @Test
    @DisplayName("測試借書功能：失敗路徑 (已被借出)")
    public void testBorrowBook_Fail_AlreadyBorrowed() {
        // 1. 準備已借出的書籍數據
        Integer bookId = 1;
        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setIsBorrowed(true);
        mockBook.setBorrowerName("別人");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        // 2. 驗證是否拋出預期的 BookNotFoundException (目前業務邏輯中借閱失敗拋出此例外)
        assertThrows(BookNotFoundException.class, () -> {
            bookService.borrowBook(bookId, "我");
        });
    }
}
