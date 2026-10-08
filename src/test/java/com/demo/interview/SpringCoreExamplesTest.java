package com.demo.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 覆蓋 {@link SpringCoreExamples}（面試示範：Spring 核心／設計模式）。
 * <br>驗證 Proxy 前後置處理與 IOC 注入切換行為。
 */
public class SpringCoreExamplesTest {

    /**
     * CASE-IV-CORE-001：Proxy 前後置處理正確。
     * <br>Given: RealSubject + ProxySubject；When: request；Then: 結果含前置／業務／後置字串。
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
     * CASE-IV-CORE-002：IOC 注入切換 Email／SMS。
     * <br>Given: EmailService／SmsService；When: NotificationProcessor.process；Then: 訊息字串依注入實作變化。
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
