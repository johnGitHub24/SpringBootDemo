package com.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 【職責】單元驗證 {@link OpenApiConfig} 組出的 OpenAPI metadata。
 * 【技巧】直接 new 設定類呼叫 {@code @Bean} 方法，不啟動 Web。
 * 【概念】與 {@code OpenApiDocsIntegrationTest} 共用 CASE-OPENAPI-001（同一 title 契約）。
 */
class OpenApiConfigTest {

    /**
     * CASE-OPENAPI-001：OpenAPI Bean title 為 SpringBootDemo API。
     * Given: OpenApiConfig；When: springBootDemoOpenApi()；Then: title／version 正確。
     */
    @Test
    void openApiBean_hasExpectedTitle() {
        OpenAPI api = new OpenApiConfig().springBootDemoOpenApi();
        assertThat(api.getInfo().getTitle()).isEqualTo("SpringBootDemo API");
        assertThat(api.getInfo().getVersion()).isEqualTo("0.0.1-SNAPSHOT");
    }
}
