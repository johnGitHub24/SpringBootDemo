package com.demo.springbootdemo.advanced.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * TCC 動作實作類別
 * 模擬分散式支付事務中的資產凍結與釋放
 */
@Slf4j
@Service
public class TccActionImpl implements TccAction {

    @Override
    public boolean prepare(BusinessActionContext actionContext, 
                           @BusinessActionContextParameter(index = 0) String orderId, 
                           @BusinessActionContextParameter(index = 1) double amount) {
        log.info("【TCC-Try】 預留資源 - 訂單號: {}, 凍結金額: {}", orderId, amount);
        // 模擬：在資料庫中將可用餘額減少，增加凍結金額
        return true; 
    }

    @Override
    public boolean confirm(BusinessActionContext actionContext) {
        String orderId = (String) actionContext.getActionContext("orderId");
        log.info("【TCC-Confirm】 確認提交 - 真正扣除訂單 {} 的金額", orderId);
        // 模擬：在資料庫中清除凍結金額，業務完成
        return true;
    }

    @Override
    public boolean cancel(BusinessActionContext actionContext) {
        String orderId = (String) actionContext.getActionContext("orderId");
        log.info("【TCC-Cancel】 取消回滾 - 釋放訂單 {} 預留的資源", orderId);
        // 模擬：將凍結金額恢復到可用餘額
        return true;
    }
}
