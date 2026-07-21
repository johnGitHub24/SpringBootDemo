package com.demo.springbootdemo.advanced.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 覆蓋 API Gateway 路由轉發（Gateway MVC + 本機模擬微服務）。
 * 使用 MockMvc（Servlet 棧）；後端 base-url 指向同埠，避免 WebTestClient／硬編碼 8080。
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=18080",
                "gateway.backend-base-url=http://127.0.0.1:18080",
                "seata.enabled=false"
        })
@AutoConfigureMockMvc(addFilters = false)
public class GatewayIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * CASE-GW-001：使用者路由轉發成功。
     * Given: Gateway 指向本機模擬 User 服務；When: GET /get-users/123；Then: 200 + id/source 正確。
     */
    @Test
    public void testUserRouteForwarding() throws Exception {
        mockMvc.perform(get("/get-users/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.source").value("Microservice_User"));
    }

    /**
     * CASE-GW-002：訂單路由轉發成功。
     * Given: Gateway 指向本機模擬 Order 服務；When: GET /get-orders/999；Then: 200 + orderId/status 正確。
     */
    @Test
    public void testOrderRouteForwarding() throws Exception {
        mockMvc.perform(get("/get-orders/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("999"))
                .andExpect(jsonPath("$.status").value("PAID"));
    }
}
