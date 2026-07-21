package com.demo.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 覆蓋 {@link SpringCloudExamples}（面試示範：Spring Cloud／微服務）。
 * 驗證 gRPC 模擬呼叫可執行與 Feign Client 介面定義存在。
 */
public class SpringCloudExamplesTest {

    /**
     * CASE-IV-CLOUD-001：gRPC 模擬呼叫不拋例外。
     * Given: MyGrpcService；When: getMySample；Then: 不拋例外。
     */
    @Test
    public void testMockGrpcCall() {
        SpringCloudExamples.MyGrpcService service = new SpringCloudExamples.MyGrpcService();
        assertDoesNotThrow(() -> service.getMySample("測試請求"));
    }

    /**
     * CASE-IV-CLOUD-002：Feign UserClient 介面已定義。
     * Given: SpringCloudExamples 內嵌介面；When: 讀取 class；Then: 非 null。
     */
    @Test
    public void testFeignDefinition() {
        // 驗證內部類別與介面定義存在
        assertNotNull(SpringCloudExamples.UserClient.class);
    }
}
