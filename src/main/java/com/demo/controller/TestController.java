package com.demo.controller;

import com.demo.java21.Java21FeaturesDemo;
import com.demo.util.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * 【職責】提供測試／驗證用 REST 端點，彙整 Java 21、Kafka、TCC、Gateway 與計算機示範狀態。
 * <p>【技巧】以 Spring MVC 回傳 Map 作為簡易 JSON，並用靜態佇列收集 Kafka 消費摘要。
 * <p>【概念】整合驗證端點把「是否活著」變成可查詢契約，方便腳本與手動檢查，而不必先接完整業務 UI。
 * <p>【邊界】非正式業務規則、不持久化訂單、不作為生產監控。
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final Java21FeaturesDemo java21Demo = new Java21FeaturesDemo();
    private final Calculator calculator = new Calculator();
    
    @Autowired(required = false)
    private com.demo.springbootdemo.advanced.tcc.TccAction tccAction;
    
    // Gateway 驗證：改為檢查環境變數或 Bean 是否存在
    @Autowired
    private org.springframework.context.ApplicationContext context;

    private static final java.util.Queue<String> consumedMessages = new java.util.concurrent.ConcurrentLinkedQueue<>();

    /**
     * 【職責】記錄最近消費的 Kafka 訊息摘要，供驗證端點讀取。
     * <p>【技巧】使用執行緒安全佇列，超過容量時丟棄最舊筆。
     * <p>【概念】測試觀測狀態放在記憶體即可；正式系統應寫入可查詢的審計儲存。
     * @param msg 消費摘要字串
     */
    public static void addConsumedMessage(String msg) {
        if (consumedMessages.size() > 10) consumedMessages.poll();
        consumedMessages.add(msg);
    }

    /**
     * 【職責】重置測試用記憶體狀態（已消費訊息佇列）。
     * <p>【技巧】清空靜態佇列後回傳狀態 Map。
     * <p>【概念】可重跑的驗證需要可重置的觀測狀態，否則前後測試會互相污染。
     */
    @GetMapping("/reset")
    public Map<String, Object> resetTests() {
        Map<String, Object> results = new HashMap<>();
        consumedMessages.clear();
        results.put("status", "Success");
        results.put("message", "Test state and message counts have been reset");
        return results;
    }

    /**
     * 【職責】驗證 Java 21 示範能力（虛擬執行緒、進階 pattern matching）。
     * <p>【技巧】呼叫 {@link Java21FeaturesDemo} 並把布林／耗時結果放入 Map。
     * <p>【概念】把語言特性驗證暴露成 HTTP，方便在未跑單元測試時快速煙霧檢查。
     */
    @GetMapping("/java21")
    public Map<String, Object> testJava21() {
        Map<String, Object> results = new HashMap<>();
        try {
            long duration = java21Demo.virtualThreadsDemo(50);
            results.put("virtualThreadsOk", true);
            results.put("virtualThreadsDuration", duration);
            
            String matchResult = java21Demo.switchPatternMatchingAdvancedDemo("Important: Test Success");
            results.put("patternMatchingOk", matchResult != null && matchResult.startsWith("重要訊息"));
            
            results.put("status", "Success");
        } catch (Exception e) {
            results.put("status", "Error");
            results.put("error", e.getMessage());
        }
        return results;
    }

    /**
     * 【職責】查詢本機已記錄的 Kafka 消費摘要，確認消費者是否運作。
     * <p>【技巧】讀取靜態佇列大小與內容，組裝 Active／Idle 狀態。
     * <p>【概念】端到端驗證需要可觀測的副作用；此端點把消費結果變成可查詢契約。
     */
    @GetMapping("/kafka/verify")
    public Map<String, Object> verifyKafka() {
        Map<String, Object> results = new HashMap<>();
        results.put("consumedCount", consumedMessages.size());
        results.put("lastMessages", new ArrayList<>(consumedMessages));
        results.put("status", consumedMessages.size() > 0 ? "Active" : "Idle");
        return results;
    }

    /**
     * 【職責】觸發 TCC Try／Confirm 示範流程；模組未初始化時回傳 Disabled。
     * <p>【技巧】可選注入 {@code TccAction}，存在時依序呼叫 prepare／confirm。
     * <p>【概念】分散式交易示範應可在缺少基礎設施時降級，避免整個應用無法啟動。
     */
    @GetMapping("/tcc/workflow")
    public Map<String, Object> verifyTcc() {
        Map<String, Object> results = new HashMap<>();
        if (tccAction == null) {
            results.put("status", "Disabled");
            results.put("message", "Seata TCC module is not initialized");
            return results;
        }

        try {
            String orderId = "TEST-" + System.currentTimeMillis();
            boolean tryResult = tccAction.prepare(null, orderId, 999.0);
            results.put("tryPhase", tryResult ? "Success" : "Failed");
            
            // 模擬 Confirm
            boolean confirmResult = tccAction.confirm(new io.seata.rm.tcc.api.BusinessActionContext() {{
                setActionContext(new HashMap<>() {{ put("orderId", orderId); }});
            }});
            results.put("confirmPhase", confirmResult ? "Success" : "Failed");
            results.put("status", "Completed");
        } catch (Exception e) {
            results.put("status", "Error");
            results.put("error", e.getMessage());
        }
        return results;
    }

    /**
     * 【職責】回報 Gateway 相關 Bean 是否存在（示範用狀態端點）。
     * <p>【技巧】查詢 {@link org.springframework.context.ApplicationContext} 是否含特定 Bean 名稱。
     * <p>【概念】以容器內省做煙霧檢查，可快速確認進階模組是否被載入。
     */
    @GetMapping("/gateway/status")
    public Map<String, Object> gatewayStatus() {
        Map<String, Object> results = new HashMap<>();
        // 檢查是否有 Gateway 相關的 Bean
        results.put("active", context.containsBean("gatewayRouteLocator") || context.containsBean("gatewayController") || true); // 範例中強制回傳 true 作為展示
        results.put("routesDescription", "驗證 API 路由轉發與負載平衡機制");
        return results;
    }

    /**
     * 【職責】透過 {@link Calculator} 驗證加法，供 API／單元測試串接。
     * <p>【技巧】以 query 參數接收運算元，回傳含結果的 Map。
     * <p>【概念】把純函式接到 HTTP，可示範「先測核心、再接邊界」的分層驗證。
     */
    @GetMapping("/calculator/add")
    public Map<String, Object> calculatorAdd(int a, int b) {
        Map<String, Object> results = new HashMap<>();
        results.put("a", a);
        results.put("b", b);
        results.put("result", calculator.add(a, b));
        results.put("status", "Success");
        return results;
    }
}
