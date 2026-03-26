package com.demo.interview;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Group C: REST 與 Web 技術範例
 * 說明 PUT vs PATCH 以及 冪等性。
 */
@RestController
@RequestMapping("/api/examples")
public class WebRestExamples {

    private final Map<Long, UserDto> userDatabase = new HashMap<>();

    public WebRestExamples() {
        // 預設資料
        userDatabase.put(1L, new UserDto(1L, "Alice", "alice@example.com"));
    }

    /**
     * PUT: 更新「全體」資源。
     * 語義上，如果請求中缺少某些欄位，原本的資料應被覆蓋或清空。
     * 是「冪等 (Idempotent)」的。
     */
    @PutMapping("/users/{id}")
    public UserDto putUpdate(@RequestBody UserDto user) {
        userDatabase.put(user.id, user); // 直接替換掉整筆資料
        return user;
    }

    /**
     * PATCH: 更新「部分」資源。
     * 僅修改請求中提供的欄位，其餘保留。
     */
    @PatchMapping("/users/{id}")
    public UserDto patchUpdate(Long id, @RequestBody Map<String, Object> updates) {
        UserDto existing = userDatabase.get(id);
        if (existing != null) {
            if (updates.containsKey("name")) existing.name = (String) updates.get("name");
            if (updates.containsKey("email")) existing.email = (String) updates.get("email");
        }
        return existing;
    }

    /*
     * [冪等性 (Idempotency) 說明]
     * - GET: 冪等 (多次讀取結果相同)。
     * - DELETE: 冪等 (刪除一次跟刪除多次，資源都不存在了)。
     * - PUT: 冪等 (多次替換同一資源，結果相同)。
     * - POST: 非冪等 (多次提交可能建立多個新資源)。
     */

    public static class UserDto {
        public Long id;
        public String name;
        public String email;

        public UserDto() {}
        public UserDto(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}
