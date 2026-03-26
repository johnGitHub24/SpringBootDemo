package com.demo.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Cloud 與 微服務範例的單元測試
 */
public class SpringCloudExamplesTest {

    @Test
    public void testMockGrpcCall() {
        SpringCloudExamples.MyGrpcService service = new SpringCloudExamples.MyGrpcService();
        assertDoesNotThrow(() -> service.getMySample("測試請求"));
    }

    @Test
    public void testFeignDefinition() {
        // 驗證內部類別與介面定義存在
        assertNotNull(SpringCloudExamples.UserClient.class);
    }
}
