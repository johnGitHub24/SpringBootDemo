package com.demo.interview;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * REST 與 Web 技術範例的單元測試
 */
public class WebRestExamplesTest {

    private final WebRestExamples controller = new WebRestExamples();

    /**
     * 測試 PUT 與 PATCH 在更新資源時的語義差異。
     * PUT 應視為全體替換，PATCH 應視為部分修正。
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
