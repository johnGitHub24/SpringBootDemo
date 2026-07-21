package com.demo.springbootdemo.advanced.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 覆蓋 {@link TccAction}（Seata TCC 動作層）的 SpringBoot 整合測試。
 * 驗證 Try／Confirm 階段可成功執行（不涵蓋 Cancel）。
 */
@SpringBootTest
public class TccActionTest {

    @Autowired
    private TccAction tccAction;

    /**
     * CASE-TCC-001：Try → Confirm 工作流成功。
     * Given: orderId=ORDER_X、amount=100；When: prepare 再 confirm；Then: 兩階段皆回 true。
     */
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
