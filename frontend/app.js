/**
 * 訂單管理系統 - 前端核心邏輯 (Vue 3 ESM 版)
 * 
 * 學習重點：
 * 1. ESM (ECMAScript Modules)：使用 import 語法引入模組，是現代開發標準。
 * 2. 響應式狀態：使用 ref 管理資料，實現 UI 與數據的同步。
 * 3. 非同步處理：使用 async / await 處理 API 請求與 YAML 匯出。
 */

// 從 CDN 引入 Vue 與 常用工具
import { createApp, ref, onMounted } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.2/+esm';
import yaml from 'https://cdn.jsdelivr.net/npm/js-yaml@4.1.0/+esm';

createApp({
    setup() {
        // --- 1. 狀態定義 (State) ---
        
        const orders = ref([]);
        const debugMode = ref(false); // 控制 Debug 面板顯示
        const currentTime = ref(new Date().toLocaleString()); // 即時間
        
        const newOrder = ref({
            customerName: '',
            productName: '',
            quantity: 1,
            price: 0,
            status: 'PENDING'
        });

        const API_BASE = 'http://localhost:8080/api/orders';

        // --- 2. 功能方法 (Methods) ---

        /**
         * 獲取所有訂單 (GET)
         */
        const fetchOrders = async () => {
            try {
                const response = await axios.get(API_BASE);
                orders.value = response.data;
                console.log('成功讀取訂單:', response.data);
            } catch (error) {
                console.error('讀取訂單失敗:', error);
                // 這裡可以加入更精確的錯誤提示
            }
        };

        /**
         * 新增訂單 (POST)
         */
        const createOrder = async () => {
            if (!newOrder.value.customerName || !newOrder.value.productName) {
                alert('請填寫完整資訊！');
                return;
            }

            try {
                await axios.post(API_BASE, newOrder.value);
                
                // 重置表單
                newOrder.value = {
                    customerName: '',
                    productName: '',
                    quantity: 1,
                    price: 0,
                    status: 'PENDING'
                };

                await fetchOrders();
                alert('訂單建立成功！');
            } catch (error) {
                console.error('建立訂單失敗:', error);
                alert('新增失敗，請檢查後端服務。');
            }
        };

        /**
         * 刪除訂單 (DELETE)
         */
        const deleteOrder = async (id) => {
            if (!confirm(`確定要刪除訂單 #${id} 嗎？`)) return;

            try {
                await axios.delete(`${API_BASE}/${id}`);
                await fetchOrders();
            } catch (error) {
                console.error('刪除失敗:', error);
                alert('刪除失敗');
            }
        };

        /**
         * 匯出訂單為 YAML 檔案
         */
        const exportToYaml = () => {
            try {
                // 將 orders 物件轉換為 YAML 文字
                const yamlContent = yaml.dump(orders.value);
                
                // 建立一個隱藏的下載連結
                const blob = new Blob([yamlContent], { type: 'text/yaml' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = `orders_${new Date().getTime()}.yaml`;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
                
                console.log('YAML 匯出完成');
            } catch (error) {
                console.error('匯出失敗:', error);
                alert('匯出 YAML 失敗');
            }
        };

        /**
         * 取得狀態對應的 CSS 樣式
         */
        const getStatusClass = (status) => {
            switch (status) {
                case 'PENDING': return 'bg-amber-100 text-amber-700';
                case 'COMPLETED': return 'bg-green-100 text-green-700';
                case 'CANCELLED': return 'bg-red-100 text-red-700';
                default: return 'bg-slate-100 text-slate-700';
            }
        };

        // --- 3. 生命週期啟動 ---

        onMounted(() => {
            fetchOrders();
            
            // 每秒更新一次時間
            setInterval(() => {
                currentTime.value = new Date().toLocaleString();
            }, 1000);
        });

        // 暴露給 HTML 的變數與方法
        return {
            orders,
            newOrder,
            debugMode,
            currentTime,
            fetchOrders,
            createOrder,
            deleteOrder,
            exportToYaml,
            getStatusClass
        };
    }
}).mount('#app');
