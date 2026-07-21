package com.demo.springbootdemo.advanced.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【職責】以教學用實作模擬支付場景的 TCC 資產凍結、扣款與釋放。
 * 【技巧】實作 {@link TccAction} 三階段，僅記錄日誌軌跡以展示語意。
 * 【概念】Try 預留、Confirm 提交、Cancel 釋放；先理解階段責任再接真實帳務寫入。
 * 【邊界】不寫入真實帳務資料庫。
 */
@Slf4j
@Service
public class TccActionImpl implements TccAction {

    /**
     * {@inheritDoc}
     * <p>教學實作僅記錄預留意圖並回傳成功，不實際異動餘額。</p>
     */
    @Override
    public boolean prepare(BusinessActionContext actionContext, 
                           @BusinessActionContextParameter(index = 0) String orderId, 
                           @BusinessActionContextParameter(index = 1) double amount) {
        log.info("【TCC-Try】 預留資源 - 訂單號: {}, 凍結金額: {}", orderId, amount);
        // 模擬：在資料庫中將可用餘額減少，增加凍結金額
        return true; 
    }

    /**
     * {@inheritDoc}
     * <p>教學實作自上下文取出訂單號並記錄確認扣款，不實際清除凍結額度。</p>
     */
    @Override
    public boolean confirm(BusinessActionContext actionContext) {
        String orderId = (String) actionContext.getActionContext("orderId");
        log.info("【TCC-Confirm】 確認提交 - 真正扣除訂單 {} 的金額", orderId);
        // 模擬：在資料庫中清除凍結金額，業務完成
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>教學實作記錄資源釋放意圖，不實際還原可用餘額。</p>
     */
    @Override
    public boolean cancel(BusinessActionContext actionContext) {
        String orderId = (String) actionContext.getActionContext("orderId");
        log.info("【TCC-Cancel】 取消回滾 - 釋放訂單 {} 預留的資源", orderId);
        // 模擬：將凍結金額恢復到可用餘額
        return true;
    }
}
