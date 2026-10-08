package com.demo.interview;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 覆蓋 {@link WebRestExamples}（面試示範：REST PUT／PATCH 語意）。
 * <br>驗證全體替換與部分更新的行為差異。
 */
public class WebRestExamplesTest {

    private final WebRestExamples controller = new WebRestExamples();

    /**
     * CASE-IV-WEB-001：PUT 全體替換、PATCH 部分更新。
     * <br>Given: 預設使用者；When: putUpdate 再 patchUpdate(name)；Then: PUT 全欄位更新，PATCH 保留未改 Email。
     */
    @Test
    public void testPutVSPatchEffect() {
        // 1. 初始化資源 (預設 ID=1 為 Alice, Email=alice@example.com)
        
        // 2. 執行 PUT 更新 (全體更新為 Bob)
        WebRestExamples.UserDto newUser = new WebRestExamples.UserDto(1L, "Bob", "bob@example.com");
        controller.putUpdate(newUser);
        
        // 驗證 PUT 結果
        WebRestExamples.UserDto userAfterPut = controller.patchUpdate(1L, new HashMap<>()); // 僅讀取
        assertEquals("Bob", userAfterPut.name, "PUT 更新姓名失敗");
        assertEquals("bob@example.com", userAfterPut.email, "PUT 更新 Email 失敗");

        // 3. 執行 PATCH 更新 (僅更新姓名為 Charlie，Email 應維持 Bob 的值)
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Charlie");
        WebRestExamples.UserDto patchedUser = controller.patchUpdate(1L, updates);

        // 驗證 PATCH 結果
        assertEquals("Charlie", patchedUser.name, "PATCH 部分更新姓名失敗");
        assertEquals("bob@example.com", patchedUser.email, "PATCH 應保留未更動的 Email 欄位");
        
        System.out.println("REST PUT/PATCH 語義驗證通過");
    }
}
