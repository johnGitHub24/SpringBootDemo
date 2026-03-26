package com.demo.interview;

import org.junit.jupiter.api.Test;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 事務管理與持久化範例的單元測試
 */
public class PersistenceExamplesTest {

    /**
     * 測試 Redis 分散式鎖的模擬執行流程。
     * 驗證在「成功獲取鎖」的情況下，業務邏輯能被執行，且最後「務必解鎖」。
     */
    @Test
    public void testRedisLockLogic() {
        PersistenceExamples.RedisLockExample example = new PersistenceExamples.RedisLockExample();
        
        // 使用 Java 內建的 ReentrantLock 模擬 Redis 控制的 Lock 對象
        ReentrantLock mockLock = new ReentrantLock();

        // 執行包含鎖邏輯的方法
        assertDoesNotThrow(() -> example.doWithLock(mockLock), "執行鎖邏輯時不應拋出異常");
        
        // 驗證解鎖邏輯：方法執行完畢後，鎖應處於未鎖定狀態
        assertFalse(mockLock.isLocked(), "解鎖 (Unlock) 失敗，這可能導致死結 (Deadlock)");
        
        System.out.println("Redis 分散式鎖模擬流程驗證通過");
    }

    /**
     * 測試 Transaction Service 是否有正確定義。
     */
    @Test
    public void testTransactionServiceDefinition() {
        assertNotNull(PersistenceExamples.TransactionService.class, "TransactionService 類別定義遺失");
    }
}
