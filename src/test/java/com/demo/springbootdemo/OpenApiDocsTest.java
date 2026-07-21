package com.demo.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 【職責】驗證 springdoc 端點：{@code /v3/api-docs} 可用且帶正確 metadata。
 * 【技巧】{@code @SpringBootTest} + MockMvc；關閉 seata 避免外部依賴。
 * 【概念】docs/swagger.html 依賴此端點，契約測試防止規格與實作漂移。
 */
@SpringBootTest(classes = SpringBootDemoApplication.class, properties = "seata.enabled=false")
@AutoConfigureMockMvc
class OpenApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * CASE-OPENAPI-001：GET /v3/api-docs 回 200 且 title 正確。
     * Given: 應用啟動；When: GET /v3/api-docs；Then: 200 + openapi 欄位 + title=SpringBootDemo API。
     */
    @Test
    void apiDocs_returnsOpenApiJson() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("SpringBootDemo API"));
    }
}
