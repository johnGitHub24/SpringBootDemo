package com.demo.api;

import com.demo.springbootdemo.SpringBootDemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 【職責】驗證 springdoc 端點 {@code /v3/api-docs} 可用且 metadata 正確。
 * <p>【技巧】{@code @SpringBootTest} + MockMvc；測試資源關閉 seata／改 simple cache。
 * <p>【概念】與 {@code OpenApiConfigTest} 共用 CASE-OPENAPI-001：單元查 Bean 標題，整合查 HTTP 契約。
 */
@SpringBootTest(classes = SpringBootDemoApplication.class, properties = {
        "seata.enabled=false",
        "spring.cache.type=simple"
})
@AutoConfigureMockMvc
class OpenApiDocsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * CASE-OPENAPI-001：GET /v3/api-docs 回 200 且 title 正確。
     * <br>Given: 應用啟動；When: GET /v3/api-docs；Then: 200 + openapi 欄位 + title=SpringBootDemo API。
     */
    @Test
    void apiDocs_returnsOpenApiJson() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("SpringBootDemo API"));
    }
}
