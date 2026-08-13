package com.demo.api;

import com.demo.springbootdemo.SpringBootDemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 【職責】訂單 REST 契約整合測試：真實 {@link com.demo.service.OrderService} + H2。
 * 【技巧】MockMvc；測試設定 {@code spring.cache.type=simple}，避免本機 Redis。
 * 【概念】與 {@code OrderServiceTest}／{@code OrderControllerTest} 共用 CASE-ORDER-* Acceptance。
 */
@SpringBootTest(classes = SpringBootDemoApplication.class, properties = {
        "seata.enabled=false",
        "spring.cache.type=simple"
})
@AutoConfigureMockMvc
class OrderApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * CASE-ORDER-001：建立訂單後可列出。
     * Given: 合法 JSON；When: POST /api/orders 再 GET /api/orders；Then: 200 + PENDING，列表為陣列。
     */
    @Test
    void createAndListOrders_returnsPending() throws Exception {
        String body = """
                {"productName":"Test Product","quantity":5,"price":99.99,"customerName":"Guest"}
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.productName").value("Test Product"));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * CASE-ORDER-002：查無訂單回 404。
     * Given: 不存在的 id；When: GET /api/orders/999999；Then: 404。
     */
    @Test
    void getMissingOrder_returns404() throws Exception {
        mockMvc.perform(get("/api/orders/999999"))
                .andExpect(status().isNotFound());
    }
}
