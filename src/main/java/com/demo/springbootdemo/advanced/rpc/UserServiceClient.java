package com.demo.springbootdemo.advanced.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 遠端程序呼叫 (RPC) 範例 - 使用 Spring Cloud OpenFeign
 * 模擬呼叫另一個微服務（例如用戶服務）
 */
@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    String getUserInfo(@PathVariable("id") Long id);
}
