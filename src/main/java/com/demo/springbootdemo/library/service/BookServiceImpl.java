package com.demo.springbootdemo.library.service;

import com.demo.springbootdemo.library.dao.BookRepository;
import com.demo.springbootdemo.library.dto.BookCreateRequest;
import com.demo.springbootdemo.library.dto.BookDto;
import com.demo.springbootdemo.library.exception.BookNotFoundException;
import com.demo.springbootdemo.library.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 圖書業務邏輯實作類別 (進階版)
 * 升級內容：
 * 1. 使用 SLF4J 日誌系統取代 System.out
 * 2. 使用 Spring Data JPA 管理資料
 * 3. 實作 DTO 轉換邏輯
 */
@Service
public class BookServiceImpl implements BookService {

    // 建立 SLF4J Logger
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Page<BookDto> getBooks(Pageable pageable) {
        log.info("正在獲取圖書列表，分頁資訊: {}", pageable);
        // 使用 Repository 的 findAll(Pageable) 自動完成分頁
        return bookRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public BookDto getBookById(Integer id) {
        return bookRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    @Transactional // 開啟事務管理
    public BookDto createBook(BookCreateRequest request) {
        log.info("正在新增書籍: {}", request.getTitle());
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    @Override
    @Transactional
    public BookDto updateBook(Integer id, BookCreateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("找不到書籍，無法更新，ID: " + id));
        
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        
        return convertToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteBook(Integer id) {
        log.warn("正在刪除書籍，ID: {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto borrowBook(Integer id, String borrowerName) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        
        if (book.getIsBorrowed()) {
            log.error("借閱失敗：書籍已借出，ID: {}", id);
            throw new BookNotFoundException("這本書已經被借走囉！借閱人：" + book.getBorrowerName());
        }

        book.setIsBorrowed(true);
        book.setBorrowerName(borrowerName);
        
        return convertToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto returnBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.setIsBorrowed(false);
        book.setBorrowerName(null);
        
        return convertToDto(bookRepository.save(book));
    }

    /**
     * 模型轉換工具：將實體 (Entity) 轉為顯示物件 (DTO)
     */
    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setIsBorrowed(book.getIsBorrowed());
        dto.setBorrowerName(book.getBorrowerName());
        return dto;
    }
}
