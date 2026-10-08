package com.demo.interview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 【職責】以註解與示意型別說明 Gateway／Feign／gRPC 等微服務通訊概念。
 * <p>【技巧】用巢狀型別與 Mock 註解呈現路由、宣告式客戶端與服務端骨架。
 * <p>【概念】微服務通訊有多種協定與閘道模式；先建立概念再接真實基礎設施，學習曲線較平緩。
 * <p>【邊界】不負責真實服務發現、負載均衡、proto 產生或正式 Feign 依賴。
 */
public class SpringCloudExamples {

    // --- (1) Spring Cloud Gateway 概念 (說明為主) ---

    /*
     * [Gateway 三大核心概念]
     * 1. Route (路由): 基本單元，含 ID, URI, Predicates, Filters。
     * 2. Predicate (斷言): 匹配條件 (例如：路徑必須是 /api/**)。
     * 3. Filter (過濾器): 在請求前後進行修改 (例如：添加 Header, 限流)。
     * 
     * 核心邏輯: 路由轉發 (Route) + 過濾器鏈 (Filter Chain)。
     */

    /**
     * 教學用 Feign 客戶端註解替身；因專案未引入 openfeign starter，僅保留宣告式客戶端語意。
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MockFeignClient {
        /**
         * @return 邏輯服務名稱（對應服務發現中的應用名）
         */
        String name();

        /**
         * @return 直連 URL；空字串表示改走服務發現（正式 Feign 行為）
         */
        String url() default "";
    }

    /**
     * 宣告式用戶服務客戶端示意：以介面＋HTTP 註解描述遠端契約，無需手寫 RestTemplate。
     */
    @MockFeignClient(name = "user-service", url = "http://localhost:8081")
    public interface UserClient {
        /**
         * 依用戶識別碼查詢遠端用戶。
         *
         * @param id 用戶主鍵
         * @return 遠端回傳的用戶資訊字串
         */
        @GetMapping("/api/users/{id}")
        String getUserById(@PathVariable("id") Long id);
    }

    // --- (3) gRPC 範例概念 ---

    /*
     * [gRPC 特性]
     * 1. 使用 HTTP/2 協定 (支援多路複用、雙向流)。
     * 2. 使用 Protocol Buffers (.proto) 作為 IDL。
     * 3. 高效序列化，適合內部微服務通訊。
     * 
     * [實作步驟]
     * 1. 撰寫 .proto 定義 Service 與 Message。
     * 2. 使用 Maven/Gradle Plugin 編譯產生 Java 類別。
     * 3. 繼承產生的 ImplBase 實作服務端 (Server)。
     * 4. 使用 Stub (Blocking/Async) 進行客戶端呼叫 (Client)。
     */

    /**
     * gRPC 服務端示意骨架（正式應繼承 proto 產生的 {@code *ImplBase}）。
     * <br>僅示範請求進入點位置，不含序列化與串流生命週期。
     */
    public static class MyGrpcService { // 實際上應繼承 ***ImplBase
        /**
         * 處理示範用 gRPC 請求；正式實作會透過 StreamObserver 回寫回應。
         *
         * @param request 客戶端請求內容（示意為字串）
         */
        public void getMySample(String request) {
            // 邏輯實作
            System.out.println("收到 gRPC 請求: " + request);
        }
    }
}
