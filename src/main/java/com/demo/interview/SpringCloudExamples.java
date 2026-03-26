package com.demo.interview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Group D: Spring Cloud 與 微服務技術範例
 * 涵蓋 Gateway 概念, OpenFeign 與 gRPC。
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
     * 由於專案未引入 spring-cloud-starter-openfeign，在此模擬註解定義。
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MockFeignClient {
        String name();
        String url() default "";
    }

    /**
     * Feign 是一個聲明式的 HTTP 客戶端。
     * 只需定義介面並加上註解，即可呼叫遠端服務。
     */
    @MockFeignClient(name = "user-service", url = "http://localhost:8081")
    public interface UserClient {
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
     * gRPC Server 示意代碼。
     */
    public static class MyGrpcService { // 實際上應繼承 ***ImplBase
        public void getMySample(String request) {
            // 邏輯實作
            System.out.println("收到 gRPC 請求: " + request);
        }
    }
}
