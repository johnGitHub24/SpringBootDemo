package com.demo.springbootdemo.library.client;

import com.demo.springbootdemo.library.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 宣告式 Feign 客戶端案例
 * 模擬呼叫外部圖書比價或資訊服務
 */
@FeignClient(name = "external-book-service", url = "https://api.example.com/v1")
public interface ExternalBookClient {

    @GetMapping("/books/{isbn}")
    BookDto getExternalBookInfo(@PathVariable("isbn") String isbn);

    @GetMapping("/books/search?query={query}")
    List<BookDto> searchBooks(@PathVariable("query") String query);
}
