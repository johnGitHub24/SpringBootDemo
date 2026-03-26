package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 這是 Web 相關的設定類別
 * 初學者筆記：@Configuration 告訴 Spring 這個類別是用來做系統設定的。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 設定 CORS (跨來源資源共用)
     * 
     * 為什麼需要這個？
     * 當你的前端頁面（例如在瀏覽器直接打開 HTML 檔案）嘗試呼叫不同來源（連接埠不同，如 8080）的 API 時，
     * 瀏覽器基於安全性會阻擋這個行為。這就是所謂的 CORS 政策。
     * 我們在這裡設定允許前端的請求通過。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 允許所有以 /api/ 開頭的路徑
            .allowedOrigins("*")       // 允許來自任何地方的請求 (開發環境常用)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 允許的 HTTP 方法
            .allowedHeaders("*");      // 允許所有的 Header 資訊
    }
}
