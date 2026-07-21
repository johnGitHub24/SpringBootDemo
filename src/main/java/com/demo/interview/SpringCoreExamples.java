package com.demo.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 【職責】以可編譯巢狀型別示範 IoC／DI／AOP 與代理模式面試概念。
 * 【技巧】展示多實作注入、{@code @Qualifier} 消歧與靜態代理擴充。
 * 【概念】控制反轉把依賴建立交給容器；代理則在不改業務碼的前提下插入橫切行為。
 * 【邊界】不負責真實 AOP 切面組態或動態代理產生。
 */
public class SpringCoreExamples {

    // --- (1) IOC (控制反轉) 與 DI (依賴注入) ---
    /*
     * DI 的主要優點是「解耦」。
     * 當我們需要更改發送訊息的方式時（例如從 Email 改為 SMS），
     * 只需要更改配置或標記，而不需要修改調用方的程式碼。
     */

    /**
     * 訊息發送策略介面；多實作並存時需以 Qualifier 或主實作消歧。
     */
    public interface MessageService {
        /**
         * @return 該通道對應的訊息描述
         */
        String getMessage();
    }

    /**
     * Email 通道實作，註冊為名為 {@code emailService} 的 Spring Bean。
     */
    @Component("emailService") // 將此類別註冊為 Spring Bean，名稱為 emailService
    public static class EmailService implements MessageService {
        @Override
        public String getMessage() {
            return "透過 Email 發送訊息";
        }
    }

    /**
     * SMS 通道實作，註冊為名為 {@code smsService} 的 Spring Bean。
     */
    @Component("smsService") // 將此類別註冊為 Spring Bean，名稱為 smsService
    public static class SmsService implements MessageService {
        @Override
        public String getMessage() {
            return "透過 SMS 發送訊息";
        }
    }

    /**
     * 示範多 {@link MessageService} 實作時以 {@code @Qualifier} 明確注入，避免 NoUniqueBeanDefinition。
     * 採建構子注入以表達必要依賴並利於測試與不變性。
     */
    @Service
    public static class NotificationProcessor {
        private final MessageService messageService;

        /**
         * @param messageService 以 Qualifier 指定的 Email 通道實作
         */
        @Autowired // Spring 會根據類型自動匹配對應的 Bean
        public NotificationProcessor(@Qualifier("emailService") MessageService messageService) {
            this.messageService = messageService;
        }

        /**
         * 組裝通知處理結果字串，實際專案會在此委派真正發送。
         *
         * @return 含通道訊息內容的處理描述
         */
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

    /**
     * 代理模式中的主體介面；真實物件與代理皆實作此契約。
     */
    public interface Subject {
        /**
         * @return 業務或代理包裝後的結果描述
         */
        String request();
    }

    /**
     * 真實主體：僅承載核心業務，不含橫切關注點。
     */
    public static class RealSubject implements Subject {
        @Override
        public String request() {
            return "執行真實業務邏輯";
        }
    }

    /**
     * 靜態代理：與真實主體共用介面，在前後插入權限檢查與日誌等橫切邏輯。
     * 優點是不改原始類即可擴充；缺點是介面方法多時代理需逐一實作（Spring AOP 可緩解）。
     */
    public static class ProxySubject implements Subject {
        private final RealSubject realSubject;

        /**
         * @param realSubject 被代理的真實業務物件
         */
        public ProxySubject(RealSubject realSubject) {
            this.realSubject = realSubject;
        }

        /**
         * 在呼叫真實業務前後附加前置／後置處理，回傳串接後的說明字串。
         *
         * @return 含代理前後處理與真實結果的描述
         */
        @Override
        public String request() {
            String pre = "代理: 檢查權限前置處理...";
            String result = realSubject.request();
            String post = "...代理: 記錄日誌後置處理";
            return pre + " -> " + result + " -> " + post;
        }
    }
}
