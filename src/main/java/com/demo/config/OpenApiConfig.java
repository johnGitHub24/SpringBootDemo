package com.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 【職責】OpenAPI（Swagger UI）文件設定：提供標題、版本與模組說明等 metadata。
 * <p>【技巧】{@code @Bean OpenAPI}；springdoc 掃描 Controller 後結合此 Bean 產生 {@code /v3/api-docs} 與 Swagger UI。
 * <p>【概念】本專案為示範用（Security 已排除），Swagger UI 無需認證即可測所有端點；
 * <br>另有手寫規格 {@code springbootdemo-api.yaml} 供離線對照，runtime 文件以本設定產生者為準。
 * <p>【邊界】不負責個別端點的 {@code @Operation} 描述（由各 Controller 註解）。
 */
@Configuration
public class OpenApiConfig {

    /**
     * 【職責】組裝 OpenAPI 文件 Bean。
     * <p>【技巧】springdoc 讀取此 Bean 產生文件首頁 metadata。
     * <p>【概念】文件與程式碼同倉，避免 API 規格與實作漂移。
     *
     * @return 設定完成的 {@link OpenAPI} 實例
     */
    @Bean
    public OpenAPI springBootDemoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringBootDemo API")
                        .version("0.0.1-SNAPSHOT")
                        .description("Order CRUD（/api/orders）、Library 示範（/library）、"
                                + "進階示範（/api/test/*、/api/ms/*）。示範用途，Security 已排除。"));
    }
}
