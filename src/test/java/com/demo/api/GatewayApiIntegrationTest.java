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
 * 【職責】Gateway MVC 路由轉發的 HTTP 整合測試（同進程模擬微服務）。
 * <p>【技巧】DEFINED_PORT + {@code gateway.backend-base-url} 指向本機，讓 http() filter 真的轉發。
 * <p>【概念】與 {@code GatewayConfigTest} 共用 CASE-GW-*：單元對照路徑常數，整合驗證實際轉發。
 */
@SpringBootTest(
        classes = SpringBootDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=18080",
                "gateway.backend-base-url=http://127.0.0.1:18080",
                "seata.enabled=false",
                "spring.cache.type=simple"
        })
@AutoConfigureMockMvc(addFilters = false)
class GatewayApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * CASE-GW-001：使用者路由轉發成功。
     * <br>Given: Gateway 指向本機模擬 User 服務；When: GET /get-users/123；Then: 200 + id/source 正確。
     */
    @Test
    void userRouteForwarding() throws Exception {
        mockMvc.perform(get("/get-users/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.source").value("Microservice_User"));
    }

    /**
     * CASE-GW-002：訂單路由轉發成功。
     * <br>Given: Gateway 指向本機模擬 Order 服務；When: GET /get-orders/999；Then: 200 + orderId/status 正確。
     */
    @Test
    void orderRouteForwarding() throws Exception {
        mockMvc.perform(get("/get-orders/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("999"))
                .andExpect(jsonPath("$.status").value("PAID"));
    }
}
