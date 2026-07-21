/**
 * SpringBoot Demo | Optimized Test Suite v3.0
 * 模組化測試引擎 - 支援場景化流程驗證與非同步等待
 */

import { createApp, ref, computed, reactive } from 'vue';

// ---------------------------------------------------------
// 1. 測試模型定義 (Test Models)
// ---------------------------------------------------------

class TestResult {
    constructor(name, desc, pass = false, logs = []) {
        this.name = name;
        this.desc = desc;
        this.pass = pass;
        this.logs = reactive(logs);
        this.timestamp = new Date();
    }
}

class TestModule {
    constructor(id, name, icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.tests = ref([]);
        this.status = computed(() => {
            if (this.tests.value.length === 0) return 'idle';
            if (this.tests.value.every(t => t.pass)) return 'pass';
            if (this.tests.value.some(t => !t.pass)) return 'fail';
            return 'running';
        });
    }

    addResult(name, desc, pass, logs = []) {
        this.tests.value.push(new TestResult(name, desc, pass, logs));
    }
}

// ---------------------------------------------------------
// 2. 測試引擎主體 (Test Engine)
// ---------------------------------------------------------

const testApp = {
    setup() {
        const HOST = 'http://localhost:8080';
        const API_BASE = `${HOST}/api/orders`;
        const running = ref(false);
        const activeModule = ref('scenario');

        // 定義模組
        const modules = reactive({
            scenario: new TestModule('scenario', '場景流程 (Scenario)', '🎭'),
            core: new TestModule('core', '核心業務 (Core)', '📦'),
            messaging: new TestModule('messaging', '中介軟體 (Kafka)', '⚡'),
            transactions: new TestModule('transactions', '分散式事務 (TCC)', '🛡️'),
            modern: new TestModule('modern', '現代語法 (Java 21)', '🚀'),
            utils: new TestModule('utils', '工具邏輯 (Utils)', '🛠️')
        });

        const allResults = computed(() => {
            return Object.values(modules).flatMap(m => m.tests.value);
        });

        const percent = computed(() => {
            const total = allResults.value.length;
            if (total === 0) return 0;
            const passed = allResults.value.filter(r => r.pass).length;
            return (passed / total) * 100;
        });

        // 輔助函式：非同步等待與重試
        const wait = (ms) => new Promise(resolve => setTimeout(resolve, ms));
        
        const retryFetch = async (url, options, validator, maxRetries = 5, delay = 1000) => {
            for (let i = 0; i < maxRetries; i++) {
                try {
                    const res = await fetch(url, options);
                    const data = await res.json();
                    if (validator(data)) return { ok: true, data };
                    await wait(delay);
                } catch (e) {
                    if (i === maxRetries - 1) throw e;
                    await wait(delay);
                }
            }
            return { ok: false, data: null };
        };

        // ---------------------------------------------------------
        // 測試執行邏輯
        // ---------------------------------------------------------

        const runTests = async () => {
            if (running.value) return;
            running.value = true;
            
            // 0. 重置後端狀態
            try {
                await fetch(`${HOST}/api/test/reset`);
            } catch (e) { console.warn('Reset failed, proceeding anyway.'); }

            // 清理舊結果
            Object.values(modules).forEach(m => m.tests.value = []);
            let testOrderId = null;

            try {
                // --- CATEGORY 1: SCENARIO (The Full Chain) ---
                activeModule.value = 'scenario';
                try {
                    // 步驟 1: 建立訂單
                    const create = await fetch(API_BASE, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ productName: '場景驗證手機', quantity: 2, price: 500, customerName: 'ScenarioTester' })
                    });
                    const order = await create.json();
                    testOrderId = order.id;
                    modules.scenario.addResult('步驟 1: 建立訂單', '全流程測試起點', !!testOrderId, [`OrderID: ${testOrderId}`]);

                    // 步驟 2: 驗證 Kafka 消費 (帶重試)
                    modules.scenario.addResult('步驟 2: Kafka 異步驗證', '等待訊息被消費者處理...', false, ['正在偵測 Kafka 訊息...']);
                    const kafkaRes = await retryFetch(`${HOST}/api/test/kafka/verify`, {}, d => d.consumedCount > 0);
                    const lastScenarioTest = modules.scenario.tests.value[modules.scenario.tests.value.length - 1];
                    lastScenarioTest.pass = kafkaRes.ok;
                    lastScenarioTest.logs.push(kafkaRes.ok ? `已收到 ${kafkaRes.data.consumedCount} 則訊息` : '等待超時');

                    // 步驟 3: 驗證 TCC 事務
                    const tcc = await fetch(`${HOST}/api/test/tcc/workflow`);
                    const tData = await tcc.json();
                    modules.scenario.addResult('步驟 3: TCC 整合驗證', '模擬 Try-Confirm 分散式一致性', tData.status === 'Completed', [`Try: ${tData.tryPhase}`, `Confirm: ${tData.confirmPhase}`]);

                } catch (e) {
                    modules.scenario.addResult('場景流程中斷', '發生錯誤', false, [e.message]);
                }

                // --- CATEGORY 2: CORE ---
                activeModule.value = 'core';
                try {
                    const health = await fetch(API_BASE);
                    modules.core.addResult('API 連線測試', '驗證基礎服務', health.ok, [`Status: ${health.status}`]);
                    
                    const get = await fetch(`${API_BASE}/${testOrderId}`);
                    const orderData = await get.json();
                    modules.core.addResult('單筆查詢 (GET)', '驗證資料讀取', orderData.id === testOrderId, [`ID: ${orderData.id}`]);

                    const del = await fetch(`${API_BASE}/${testOrderId}`, { method: 'DELETE' });
                    modules.core.addResult('訂單刪除 (DELETE)', '驗證資源釋放', del.ok, [`Status: ${del.status}`]);
                } catch (e) {
                    modules.core.addResult('核心模組異常', '通訊失敗', false, [e.message]);
                }

                // --- CATEGORY 3: MESSAGING ---
                activeModule.value = 'messaging';
                try {
                    const kafka = await fetch(`${HOST}/api/test/kafka/verify`);
                    const kData = await kafka.json();
                    modules.messaging.addResult('Kafka 狀態檢查', '驗證消費者監聽狀態', kData.status === 'Active' || kData.status === 'Idle', [`Consumed: ${kData.consumedCount}`]);
                } catch (e) {
                    modules.messaging.addResult('Kafka 模組故障', 'API 異常', false, [e.message]);
                }

                // --- CATEGORY 4: MODERN ---
                activeModule.value = 'modern';
                try {
                    const java21 = await fetch(`${HOST}/api/test/java21`);
                    const jData = await java21.json();
                    modules.modern.addResult('Virtual Threads', 'Java 21 並發模型', jData.virtualThreadsOk, [`Duration: ${jData.virtualThreadsDuration}ms`]);
                    modules.modern.addResult('Pattern Matching', 'Switch 模式匹配', jData.patternMatchingOk, [`Result: Success`]);
                } catch (e) {
                    modules.modern.addResult('Java 21 支援異常', '環境檢核失敗', false, [e.message]);
                }

                // --- CATEGORY 5: UTILS ---
                activeModule.value = 'utils';
                try {
                    const calc = await fetch(`${HOST}/api/test/calculator/add?a=20&b=22`);
                    const cData = await calc.json();
                    modules.utils.addResult('Calculator Logic', '驗證基礎運算 (20+22)', cData.result === 42, [`Got: ${cData.result}`]);
                    
                    const gateway = await fetch(`${HOST}/api/test/gateway/status`);
                    const gData = await gateway.json();
                    modules.utils.addResult('Gateway Proxy', '驗證 API 閘道轉發', gData.active, [`Active: ${gData.active}`]);
                } catch (e) {
                    modules.utils.addResult('工具類測試失敗', 'API 端點異常', false, [e.message]);
                }

            } catch (err) {
                console.error('Critical Suite Failure:', err);
            }

            running.value = false;
        };

        // 自動執行
        runTests();

        return {
            modules,
            running,
            activeModule,
            allResults,
            percent,
            runTests
        };
    }
};

createApp(testApp).mount('#test-app');
