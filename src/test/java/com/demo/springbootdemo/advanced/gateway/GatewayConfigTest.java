package com.demo.springbootdemo.advanced.gateway;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 【職責】單元驗證 Gateway 路徑改寫常數與整合層實際轉發 URI 一致。
 * <p>【技巧】只斷言公開常數，不啟動容器、不打 HTTP。
 * <p>【概念】與 {@code GatewayApiIntegrationTest} 共用 CASE-GW-001／002。
 */
class GatewayConfigTest {

    /**
     * CASE-GW-001：使用者路由改寫契約。
     * <br>Given: USER_* 常數；When: segment=123；Then: 下游為 /api/ms/users/123。
     */
    @Test
    void userProxyRewritesSegmentToMsUsers() {
        assertThat(GatewayConfig.USER_PROXY_PATTERN).isEqualTo("/get-users/{segment}");
        assertThat(GatewayConfig.USER_DOWNSTREAM_PATTERN.replace("{segment}", "123"))
                .isEqualTo("/api/ms/users/123");
    }

    /**
     * CASE-GW-002：訂單路由改寫契約。
     * <br>Given: ORDER_* 常數；When: segment=999；Then: 下游為 /api/ms/orders/999。
     */
    @Test
    void orderProxyRewritesSegmentToMsOrders() {
        assertThat(GatewayConfig.ORDER_PROXY_PATTERN).isEqualTo("/get-orders/{segment}");
        assertThat(GatewayConfig.ORDER_DOWNSTREAM_PATTERN.replace("{segment}", "999"))
                .isEqualTo("/api/ms/orders/999");
    }
}
