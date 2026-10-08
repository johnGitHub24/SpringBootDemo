package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 【職責】設定 Web MVC 的跨來源（CORS）規則，讓前端開發時可呼叫本機 API。
 * <p>【技巧】實作 {@link WebMvcConfigurer#addCorsMappings}，對 {@code /api/**} 宣告允許的來源、方法與 Header。
 * <p>【概念】瀏覽器同源政策會阻擋不同埠的前端呼叫；開發期可用寬鬆 CORS，正式環境應改為明確白名單來源。
 * <p>【邊界】不處理認證授權，也不決定業務 API 契約。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 【職責】註冊開發用 CORS 對應，放行 API 路徑的跨來源請求。
     * <p>【技巧】透過 {@link CorsRegistry} 鏈式設定 origins／methods／headers。
     * <p>【概念】CORS 是瀏覽器安全機制，不是伺服器防火牆；後端仍需自行做認證與授權。
     * @param registry Spring CORS 登錄表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 允許所有以 /api/ 開頭的路徑
            .allowedOrigins("*")       // 允許來自任何地方的請求 (開發環境常用)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 允許的 HTTP 方法
            .allowedHeaders("*");      // 允許所有的 Header 資訊
    }
}
