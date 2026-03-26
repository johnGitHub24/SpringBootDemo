package com.demo.springbootdemo.advanced.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * API Gateway 整合測試
 * 測試路由轉發功能是否正常運作
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "seata.enabled=false")
public class GatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testUserRouteForwarding() {
        // 測試訪問 Gateway 端點 /get-users/123 
        // 預期應轉發至模擬服務並返回正確的 JSON 內容
        webTestClient.get().uri("/get-users/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123")
                .jsonPath("$.source").isEqualTo("Microservice_User");
    }

    @Test
    public void testOrderRouteForwarding() {
        // 測試訂單服務轉發
        webTestClient.get().uri("/get-orders/999")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo("999")
                .jsonPath("$.status").isEqualTo("PAID");
    }
}
