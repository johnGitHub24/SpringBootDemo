package com.demo.springbootdemo.library.controller;

import com.demo.springbootdemo.library.dto.BookDto;
import com.demo.springbootdemo.library.exception.BookNotFoundException;
import com.demo.springbootdemo.library.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 覆蓋 {@link BookController}（Controller 層）的 MockMvc 整合測試。
 * Service 以 {@code @MockBean} 隔離，驗證 HTTP 狀態碼與 JSON 錯誤回應。
 */
@SpringBootTest(properties = "seata.enabled=false")
@AutoConfigureMockMvc // 自動配置 MockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService; // 模擬 Service 層

    /**
     * CASE-BOOK-CTL-001：取得書籍成功。
     * Given: Service 回傳 BookDto；When: GET /library/books/1；Then: 200 + title/id 正確。
     */
    @Test
    @DisplayName("測試獲取書籍 API：成功")
    public void testGetBook_Success() throws Exception {
        // 1. 模擬 Service 回傳數據
        BookDto mockBook = new BookDto();
        mockBook.setId(1);
        mockBook.setTitle("Spring 測試");

        Mockito.when(bookService.getBookById(1)).thenReturn(mockBook);

        // 2. 發送模擬請求並驗證 (Fluent API)
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/books/1");
        
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()) // 驗證是否為 200 OK
                .andExpect(jsonPath("$.title").value("Spring 測試")) // 驗證 JSON 內容
                .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * CASE-BOOK-CTL-002：書籍不存在回 404。
     * Given: Service 拋出 BookNotFoundException；When: GET /library/books/999；Then: 404 + 錯誤訊息。
     */
    @Test
    @DisplayName("測試獲取書籍 API：找不到書籍 (404)")
    public void testGetBook_NotFound() throws Exception {
        // 模擬 Service 拋出自定義例外
        Mockito.when(bookService.getBookById(999)).thenThrow(new BookNotFoundException(999));

        mockMvc.perform(MockMvcRequestBuilders.get("/library/books/999"))
                .andExpect(status().isNotFound()) // 驗證是否為 404
                .andExpect(jsonPath("$.message").value("找不到書籍，ID: 999"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
