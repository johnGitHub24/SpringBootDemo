package com.demo.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring 核心與設計模式範例的單元測試
 */
public class SpringCoreExamplesTest {

    /**
     * 測試代理模式 (Proxy Pattern) 的執行邏輯。
     * 驗證代理對象是否正確地在目標對象執行前後加入了額外的處理。
     */
    @Test
    public void testProxyPattern() {
        SpringCoreExamples.RealSubject real = new SpringCoreExamples.RealSubject();
        SpringCoreExamples.ProxySubject proxy = new SpringCoreExamples.ProxySubject(real);

        String result = proxy.request();

        // 斷言中包含繁體中文說明
        assertTrue(result.contains("檢查權限前置處理"), "代理應包含前置處理邏輯");
        assertTrue(result.contains("執行真實業務邏輯"), "代理應正確呼叫目標對象的邏輯");
        assertTrue(result.contains("記錄日誌後置處理"), "代理應包含後置處理邏輯");
        
        System.out.println("代理模式驗證通過: " + result);
    }

    /**
     * 測試 IOC (控制反轉) 的模擬邏輯。
     * 驗證 NotificationProcessor 是否能根據注入的 Service 產生正確的訊息。
     */
    @Test
    public void testNotificationProcessorLogic() {
        // 使用 EmailService 模擬注入
        SpringCoreExamples.MessageService emailService = new SpringCoreExamples.EmailService();
        SpringCoreExamples.NotificationProcessor emailProcessor = new SpringCoreExamples.NotificationProcessor(emailService);
        assertEquals("處理中: 透過 Email 發送訊息", emailProcessor.process(), "Email 通知處理邏輯錯誤");

        // 使用 SmsService 模擬注入
        SpringCoreExamples.MessageService smsService = new SpringCoreExamples.SmsService();
        SpringCoreExamples.NotificationProcessor smsProcessor = new SpringCoreExamples.NotificationProcessor(smsService);
        assertEquals("處理中: 透過 SMS 發送訊息", smsProcessor.process(), "SMS 通知處理邏輯錯誤");
        
        System.out.println("IOC 注入邏輯驗證通過");
    }
}
