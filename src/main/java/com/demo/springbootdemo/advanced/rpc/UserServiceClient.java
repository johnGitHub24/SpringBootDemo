package com.demo.springbootdemo.advanced.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 【職責】以 OpenFeign 宣告式呼叫遠端用戶服務。
 * 【技巧】將 HTTP 呼叫抽象為介面方法，綁定服務名與示範 URL。
 * 【概念】Feign 讓跨服務呼叫看起來像本地方法，可減少手寫 HTTP 客戶端樣板。
 * 【邊界】不負責重試／熔斷細節或回應後的業務規則；正式環境請改服務發現或環境變數。
 */
@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    /**
     * 【職責】依用戶識別碼查詢遠端用戶資訊。
     * 【技巧】路徑變數綁定 id，回傳字串（教學簡化；實務多為 DTO）。
     * 【概念】客戶端契約應與遠端 API 對齊，變更時兩邊一起演進。
     */
    @GetMapping("/api/users/{id}")
    String getUserInfo(@PathVariable("id") Long id);
}
