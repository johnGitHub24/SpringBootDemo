package com.demo.springbootdemo.advanced.ms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 【職責】模擬「用戶微服務」下游，供 Gateway／Feign 教學轉發驗證。
 * <p>【技巧】薄 {@code @RestController} 依路徑參數回傳固定結構示範資料。
 * <p>【概念】下游契約穩定後，閘道與 Feign 客戶端才能獨立演進與測試。
 * <p>【邊界】不負責真實帳號體系、持久化或權限控管。
 */
@RestController
@RequestMapping("/api/ms/users")
public class SimulatedUserController {

    /**
     * 【職責】依用戶識別碼回傳示範用戶資料。
     * <p>【技巧】以 {@code Map.of} 組裝固定欄位 JSON。
     * <p>【概念】教學環境用假資料驗證轉發路徑，比先建完整用戶服務更快形成回饋。
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable String id) {
        return Map.of(
            "id", id,
            "name", "User_" + id,
            "role", "DEVELOPER",
            "source", "Microservice_User"
        );
    }
}
