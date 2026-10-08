package com.demo.interview;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 【職責】以記憶體 Map 示範 PUT／PATCH 語意與冪等性差異的 REST 面試範例。
 * <p>【技巧】薄 {@code @RestController} 對照全量替換與部分更新的 HTTP 行為。
 * <p>【概念】PUT 通常表示完整替換且冪等；PATCH 表示部分更新。先用記憶體模型理解語意再接資料庫。
 * <p>【邊界】不負責持久化、驗證或真實用戶領域規則。
 */
@RestController
@RequestMapping("/api/examples")
public class WebRestExamples {

    private final Map<Long, UserDto> userDatabase = new HashMap<>();

    /**
     * 初始化一筆示範用戶，供 PUT／PATCH 對照操作。
     */
    public WebRestExamples() {
        // 預設資料
        userDatabase.put(1L, new UserDto(1L, "Alice", "alice@example.com"));
    }

    /**
     * PUT：以請求本體全量替換資源；缺欄位語意上應覆蓋／清空，且為冪等操作。
     *
     * @param user 完整用戶資源本體（含 id）
     * @return 替換後的用戶資源
     */
    @PutMapping("/users/{id}")
    public UserDto putUpdate(@RequestBody UserDto user) {
        userDatabase.put(user.id, user); // 直接替換掉整筆資料
        return user;
    }

    /**
     * PATCH：僅套用請求中出現的欄位，其餘保留；適合部分更新場景。
     *
     * @param id      目標用戶識別碼
     * @param updates 欲變更的欄位鍵值（如 name、email）
     * @return 更新後的用戶；若不存在則為 {@code null}
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

    /**
     * 面試用用戶傳輸物件；欄位公開以便示範 JSON 綁定，非正式領域模型。
     */
    public static class UserDto {
        public Long id;
        public String name;
        public String email;

        /** 供 Jackson／框架反序列化使用的無參建構子。 */
        public UserDto() {}

        /**
         * @param id    用戶識別碼
         * @param name  顯示名稱
         * @param email 電子郵件
         */
        public UserDto(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}
