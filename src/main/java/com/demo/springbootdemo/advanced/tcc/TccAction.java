package com.demo.springbootdemo.advanced.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 【職責】定義 TCC（Try-Confirm-Cancel）資源動作契約，供 Seata 在全域事務中調度。
 * <p>【技巧】以 {@code @LocalTCC}／{@code @TwoPhaseBusinessAction} 宣告兩階段回呼與上下文參數。
 * <p>【概念】TCC 用「先預留、再確認或取消」達成跨服務最終一致，比單庫本地交易更適合分散式協調。
 * <p>【邊界】不負責開啟全域事務或 MQ 通知；持久化細節由實作類處理。
 */
@LocalTCC
public interface TccAction {

    /**
     * 第一階段 Try：檢查並預留業務資源（例如將金額轉入凍結），失敗時應回傳 false 以觸發全域回滾。
     *
     * @param actionContext 事務上下文，承載 xid 與後續階段可讀取的參數
     * @param orderId       訂單編號，寫入上下文供 Confirm／Cancel 使用
     * @param amount        欲預留的金額
     * @return {@code true} 表示預留成功；{@code false} 表示預留失敗
     */
    @TwoPhaseBusinessAction(name = "TccAction", commitMethod = "confirm", rollbackMethod = "cancel")
    boolean prepare(BusinessActionContext actionContext, 
                    @BusinessActionContextParameter(index = 0) String orderId, 
                    @BusinessActionContextParameter(index = 1) double amount);

    /**
     * 第二階段 Confirm：在所有參與者 Try 成功後，由 Seata 呼叫以真正消耗已預留資源。
     *
     * @param actionContext 含 Try 階段寫入參數的事務上下文
     * @return {@code true} 表示確認成功
     */
    boolean confirm(BusinessActionContext actionContext);

    /**
     * 第二階段 Cancel：任一參與者失敗時，由 Seata 呼叫以釋放已預留資源，維持最終一致性。
     *
     * @param actionContext 含 Try 階段寫入參數的事務上下文
     * @return {@code true} 表示回滾成功
     */
    boolean cancel(BusinessActionContext actionContext);
}
