package com.demo.springbootdemo.advanced.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 分散式事務 TCC 動作定義 (Try-Confirm-Cancel)
 * 
 * 技術原理 (最終一致性)：
 * 1. Try: 檢查且預留業務資源。
 * 2. Confirm: 確認執行。
 * 3. Cancel: 釋放預留的業務資源。
 * 
 * 適用場景：跨服務的原子性操作，例如：扣款成功同時要增加積分。
 */
@LocalTCC
public interface TccAction {

    /**
     * 第一階段：Try
     * 核心邏輯：預留。例如支付 100 元，此步驟會將 100 元轉入「凍結金額」。
     * 
     * @param actionContext 事務上下文，存儲事務 ID (xid)
     * @param orderId 訂單編號
     * @param amount 預留金額
     */
    @TwoPhaseBusinessAction(name = "TccAction", commitMethod = "confirm", rollbackMethod = "cancel")
    boolean prepare(BusinessActionContext actionContext, 
                    @BusinessActionContextParameter(index = 0) String orderId, 
                    @BusinessActionContextParameter(index = 1) double amount);

    /**
     * 第二階段：Confirm
     * 核心邏輯：執行。若所有 Try 皆成功，Seata 會調用此方法將「凍結金額」真正扣除。
     */
    boolean confirm(BusinessActionContext actionContext);

    /**
     * 第二階段：Cancel
     * 核心邏輯：回滾。若有任何 Try 失敗，Seata 會調用此方法將「凍結金額」退回到可用餘額。
     */
    boolean cancel(BusinessActionContext actionContext);
}
