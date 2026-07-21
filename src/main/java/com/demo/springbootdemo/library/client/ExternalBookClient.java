package com.demo.springbootdemo.library.client;

import com.demo.springbootdemo.library.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 【職責】宣告式呼叫外部圖書資訊 API，回傳形狀對齊本系統 {@link BookDto}。
 * 【技巧】以 {@code @FeignClient} 描述遠端服務名稱與 URL，方法對應 HTTP 路徑。
 * 【概念】Feign 把 HTTP 客戶端寫成介面，可減少手寫 RestTemplate／WebClient 樣板。
 * 【邊界】不負責本地庫存、借還或持久化；實際 URL 由組態決定。
 */
@FeignClient(name = "external-book-service", url = "https://api.example.com/v1")
public interface ExternalBookClient {

    /**
     * 【職責】依 ISBN 向外部服務查詢單本圖書資訊。
     * 【技巧】路徑變數綁定 isbn。
     * 【概念】遠端查詢與本地借還分離，可讓比價／ enrichment 獨立演進。
     */
    @GetMapping("/books/{isbn}")
    BookDto getExternalBookInfo(@PathVariable("isbn") String isbn);

    /**
     * 【職責】依關鍵字向外部服務搜尋圖書。
     * 【技巧】以 query 路徑參數傳遞搜尋字串。
     * 【概念】搜尋契約由外部 API 定義；本系統只做轉接與 DTO 對齊。
     */
    @GetMapping("/books/search?query={query}")
    List<BookDto> searchBooks(@PathVariable("query") String query);
}
