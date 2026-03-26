package com.demo.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Group B: Spring 核心與設計模式範例
 * 
 * 本類別旨在展示 Springboot 的三大核心特性：
 * 1. IoC (Inversion of Control) - 控制反轉：將物件的創建與依賴管理交給 Spring 容器。
 * 2. DI (Dependency Injection) - 依賴注入：IoC 的具體實現方式，動態地將相依物件傳遞入目標物件。
 * 3. AOP (Aspect Oriented Programming) - 面向切面程式設計：在不修改原始碼的情況下，橫向切入日誌、權限、事務等功能。
 */
public class SpringCoreExamples {

    // --- (1) IOC (控制反轉) 與 DI (依賴注入) ---
    /*
     * DI 的主要優點是「解耦」。
     * 當我們需要更改發送訊息的方式時（例如從 Email 改為 SMS），
     * 只需要更改配置或標記，而不需要修改調用方的程式碼。
     */

    public interface MessageService {
        String getMessage();
    }

    @Component("emailService") // 將此類別註冊為 Spring Bean，名稱為 emailService
    public static class EmailService implements MessageService {
        @Override
        public String getMessage() {
            return "透過 Email 發送訊息";
        }
    }

    @Component("smsService") // 將此類別註冊為 Spring Bean，名稱為 smsService
    public static class SmsService implements MessageService {
        @Override
        public String getMessage() {
            return "透過 SMS 發送訊息";
        }
    }

    /**
     * 演示 @Qualifier 的作用。
     * 當一個介面有多個實現（如 EmailService 與 SmsService）時，
     * Spring 會不知道要注入哪一個。此時透過 @Qualifier("beanName") 明確指定。
     * 
     * [注入方式建議]：官方推薦使用「建構子注入 (Constructor Injection)」，
     * 因為這樣可以明確表達必要依賴，且有利於測試與不變性（使用 final）。
     */
    @Service
    public static class NotificationProcessor {
        private final MessageService messageService;

        @Autowired // Spring 會根據類型自動匹配對應的 Bean
        public NotificationProcessor(@Qualifier("emailService") MessageService messageService) {
            this.messageService = messageService;
        }

        public String process() {
            return "處理中: " + messageService.getMessage();
        }
    }

    // --- (2) AOP (面向切面) 概念 (說明為主) ---

    /*
     * [AOP 術語說明]
     * - Aspect (切面): 跨越多個物件的關注點（如日誌）。
     * - Advice (通知): 切面在特定連接點採取的動作。
     *   - @Before: 目標方法執行前執行。
     *   - @After: 目標方法執行後執行。
     *   - @Around: 最強大的通知，可環繞目標方法的執行。
     * - JoinPoint (連接點): 方法執行過程中的某個點。
     * - PointCut (切入點): 匹配連接點的斷言（定義 Advice 應在哪裡作用）。
     * 
     * [Spring AOP 底層]：
     * Spring AOP 使用動態代理技術。
     * 1. 如果目標物件實現了介面，預設使用 JDK Dynamic Proxy。
     * 2. 如果目標物件沒有實現介面，則使用 CGLIB (Code Generation Library) 生產子類進行代理。
     */

    // --- (3) 代理模式 (Proxy Pattern) ---

    public interface Subject {
        String request();
    }

    public static class RealSubject implements Subject {
        @Override
        public String request() {
            return "執行真實業務邏輯";
        }
    }

    /**
     * 靜態代理範例 (Static Proxy)。
     * 代理類別與被代理類別實現相同的介面。
     * 優點：在不修改原始類別的情況下，擴充功能（如檢查權限、緩存、日誌）。
     * 缺點：如果介面方法很多，代理類別也要一個個實現，代碼冗餘較大（Spring AOP 解決了這個問題）。
     */
    public static class ProxySubject implements Subject {
        private final RealSubject realSubject;

        public ProxySubject(RealSubject realSubject) {
            this.realSubject = realSubject;
        }

        @Override
        public String request() {
            String pre = "代理: 檢查權限前置處理...";
            String result = realSubject.request();
            String post = "...代理: 記錄日誌後置處理";
            return pre + " -> " + result + " -> " + post;
        }
    }
}
