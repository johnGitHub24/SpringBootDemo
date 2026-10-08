package com.demo.model;

/**
 * 【職責】定義訂單生命週期中可持久化的狀態。
 * <p>【技巧】由 {@link Order} 以 {@code EnumType.STRING} 保存列舉名稱，避免序號漂移。
 * <p>【概念】狀態列舉集中表達有限生命週期，較自由字串更容易驗證與維護資料相容性。
 * <p>【邊界】不負責判定何時可轉換狀態；規則由服務層決定。
 */
public enum OrderStatus {
    /** 待處理：新建後的預設狀態。 */
    PENDING,
    /** 已完成：訂單流程結束。 */
    COMPLETED,
    /** 已取消：訂單作廢。 */
    CANCELLED
}
