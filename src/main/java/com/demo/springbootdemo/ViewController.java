package com.demo.springbootdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 【職責】將瀏覽器路徑對應到伺服端視圖名稱（訂單示範頁）。
 * <p>【技巧】使用 {@code @Controller}（非 RestController）回傳視圖名稱字串，由視圖解析器渲染。
 * <p>【概念】MVC 視圖入口與 REST JSON API 分離，可讓同一後端同時服務頁面與 AJAX。
 * <p>【邊界】不回傳 JSON、不執行商業規則；圖書 API 見 {@link com.demo.springbootdemo.library.controller.BookController}。
 */
@Controller
public class ViewController {

    /**
     * 【職責】根路徑導向訂單示範頁。
     * <p>【技巧】{@code @GetMapping("/")} 回傳邏輯視圖名 {@code orders}。
     * <p>【概念】回傳字串不是檔名路徑，而是交由 Thymeleaf／視圖解析器解析的邏輯名稱。
     */
    @GetMapping("/")
    public String index() {
        return "orders";
    }

    /**
     * 【職責】提供 {@code /orders} 別名路徑，與根路徑共用同一視圖。
     * <p>【技巧】第二個映射回傳相同視圖名稱，避免重複樣板。
     * <p>【概念】多個 URL 可對應同一視圖，方便書籤與導覽連結。
     */
    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }
}
