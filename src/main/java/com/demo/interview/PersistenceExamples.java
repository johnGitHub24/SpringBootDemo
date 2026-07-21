package com.demo.interview;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 【職責】示範事務傳播、分散式鎖骨架與資料庫鎖／隔離等級概念。
 * 【技巧】以 {@code @Transactional} 傳播設定與鎖使用骨架對照悲觀／樂觀鎖說明。
 * 【概念】一致性問題常同時涉及交易邊界與鎖策略；範例把關鍵旋鈕集中展示以便面試對照。
 * 【邊界】不負責真實資料存取、鎖註冊表整合或死鎖重試框架。
 */
public class PersistenceExamples {

    // --- (1) Spring Transaction 傳播屬性 (說明為主) ---

    /*
     * [常見傳播類型]
     * 1. REQUIRED (預設): 支援當前事務，若無則建新的。
     * 2. REQUIRES_NEW: 無論有無，都建立新事務，並掛起當前事務。
     * 3. SUPPORTS: 支援當前事務，若無則以非事務方式執行。
     * 4. MANDATORY: 支援當前事務，若無則拋出異常。
     * 5. NESTED: 如果存在事務，則在嵌套事務中執行。
     */

    /**
     * 事務傳播屬性示範服務：對照 REQUIRED 與 REQUIRES_NEW 的宣告方式。
     * 方法本體刻意留空，重點在註解語意而非業務。
     */
    @Service
    public static class TransactionService {
        
        /**
         * 加入既有事務或新建事務（REQUIRED），隔離等級為讀已提交。
         */
        @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
        public void requiredMethod() {
            // 資料庫操作
        }

        /**
         * 一律開啟獨立新事務（REQUIRES_NEW），與外層事務互不提交綁定。
         */
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void requiresNewMethod() {
            // 獨立的交易
        }
    }

    // --- (2) Redis 分散式鎖 (示意代碼) ---

    /**
     * Redis 分散式鎖使用骨架：以 {@link Lock#tryLock} 限時搶鎖，並在 finally 釋放。
     * 實際鎖實例通常來自 RedisLockRegistry；此處僅示範正確的取得／釋放邊界。
     */
    public static class RedisLockExample {
        /**
         * 在限時內取得鎖後執行關鍵區段；取得失敗則略過，中斷時還原中斷旗標。
         *
         * @param lock 由外部注入的分散式鎖實例
         */
        public void doWithLock(Lock lock) {
            try {
                // tryLock(等待時間, 單位)
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    try {
                        // 執行關鍵業務邏輯 (例如：扣減庫存)
                        System.out.println("成功獲取鎖，處理業務中...");
                    } finally {
                        lock.unlock(); // 務必解鎖
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // --- (3) 悲觀鎖 vs 樂觀鎖 ---

    /*
     * [悲觀鎖 (Pessimistic Lock)]
     * - 實作: SELECT * FROM table WHERE id = 1 FOR UPDATE;
     * - 優點: 保證強一致性。
     * - 缺點: 效能較差，易造成死結或請求阻塞。
     * 
     * [樂觀鎖 (Optimistic Lock)]
     * - 實作: 透過 version 欄位。UPDATE table SET data = ?, version = version + 1 WHERE id = 1 AND version = 5;
     * - 優點: 無須鎖定資料庫行，吞吐量高。
     * - 缺點: 高度競爭下容易失敗，需重試機制。
     */

    // --- (4) 資料庫隔離等級 ---

    /*
     * 1. Read Uncommitted: 可能發生 Dirty Read。
     * 2. Read Committed: 解決 Dirty Read，但可能 Non-repeatable Read。
     * 3. Repeatable Read (MySQL 預設): 解決 Non-repeatable Read，可能發生 Phantom Read (MySQL InnoDB 已解決)。
     * 4. Serializable: 最高等級，完全排列執行，效能最低。
     */
}
