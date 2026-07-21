package com.demo.interview;

import org.junit.jupiter.api.Test;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 覆蓋 {@link PersistenceExamples}（面試示範：事務／分散式鎖）。
 * 驗證 Redis 鎖模擬流程與 TransactionService 類別定義存在。
 */
public class PersistenceExamplesTest {

    /**
     * CASE-IV-PERS-001：Redis 鎖模擬執行後必解鎖。
     * Given: ReentrantLock 模擬；When: doWithLock；Then: 不拋例外且 isLocked=false。
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
     * CASE-IV-PERS-002：TransactionService 類別已定義。
     * Given: PersistenceExamples 內嵌類；When: 讀取 class；Then: 非 null。
     */
    @Test
    public void testTransactionServiceDefinition() {
        assertNotNull(PersistenceExamples.TransactionService.class, "TransactionService 類別定義遺失");
    }
}
