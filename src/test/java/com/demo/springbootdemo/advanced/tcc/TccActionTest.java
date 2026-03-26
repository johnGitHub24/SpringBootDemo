package com.demo.springbootdemo.advanced.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Seata TCC 邏輯測試
 * 驗證 TCC 動作各階段的執行狀況
 */
@SpringBootTest
public class TccActionTest {

    @Autowired
    private TccAction tccAction;

    @Test
    public void testTccWorkflow() {
        // 1. 測試 Try 階段
        boolean prepareResult = tccAction.prepare(null, "ORDER_X", 100.0);
        assertThat(prepareResult).isTrue();

        // 2. 模擬 Confirm 階段 (手動建立上下文)
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("orderId", "ORDER_X");
        BusinessActionContext context = new BusinessActionContext();
        context.setActionContext(contextMap);
        
        boolean confirmResult = tccAction.confirm(context);
        assertThat(confirmResult).isTrue();
    }
}
