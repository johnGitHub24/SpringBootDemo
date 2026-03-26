/**
 * 這是 Vue 3 的應用程式邏輯
 * 
 * 學習點：
 * 1. createApp：建立一個 Vue 應用實例。
 * 2. ref：建立響應式數據（Reactive Data），當數據改變時，網頁會自動更新。
 * 3. onMounted：生命週期鉤子，當網頁加載完成後自動執行。
 */
const { createApp, ref, onMounted } = Vue;

createApp({
    setup() {
        // --- 狀態定義 (Data) ---

        // 存放所有從後端抓回來的訂單
        const orders = ref([]);

        // 用來存放「新增訂單」表單裡面的資料
        const newOrder = ref({
            customerName: '',
            productName: '',
            quantity: 1,
            price: 0,
            status: '已建立'
        });

        // 後端 API 的基礎網址
        const API_BASE = 'http://localhost:8080/api/orders';

        // --- 方法定義 (Methods) ---

        /**
         * 向後端獲取所有訂單 (GET)
         */
        const fetchOrders = async () => {
            try {
                // 使用 axios.get 呼叫 Spring Boot 的 API
                const response = await axios.get(API_BASE);
                // 將回傳的資料放進 orders 變數，Vue 會自動幫你畫在網頁上
                orders.value = response.data;
                console.log('成功讀取訂單:', response.data);
            } catch (error) {
                console.error('讀取訂單失敗:', error);
                alert('無法連接後端伺服器，請確保 Spring Boot 已啟動並設定 CORS。');
            }
        };

        /**
         * 新增一筆訂單 (POST)
         */
        const createOrder = async () => {
            // 基礎檢查
            if (!newOrder.value.customerName || !newOrder.value.productName) {
                alert('請填寫客戶名稱與商品名稱');
                return;
            }

            try {
                // 使用 axios.post 將表單資料傳送給後端
                const response = await axios.post(API_BASE, newOrder.value);
                console.log('訂單建立成功:', response.data);

                // 清空表單
                newOrder.value = {
                    customerName: '',
                    productName: '',
                    quantity: 1,
                    price: 0,
                    status: '已建立'
                };

                // 重新刷新列表
                await fetchOrders();
                alert('訂單新增成功！');
            } catch (error) {
                console.error('建立訂單失敗:', error);
                alert('新增失敗，請檢查後端日誌。');
            }
        };

        /**
         * 刪除一筆訂單 (DELETE)
         */
        const deleteOrder = async (id) => {
            if (!confirm(`確定要刪除 ID 為 ${id} 的訂單嗎？`)) return;

            try {
                // 使用 axios.delete 呼叫刪除 API
                await axios.delete(`${API_BASE}/${id}`);
                // 重新刷新列表
                await fetchOrders();
                console.log(`訂單 ${id} 已刪除`);
            } catch (error) {
                console.error('刪除訂單失敗:', error);
                alert('刪除失敗');
            }
        };

        // 當網頁一打開，立刻去抓資料
        onMounted(() => {
            fetchOrders();
        });

        // 這裡將資料與方法「暴露」給 HTML 使用
        return {
            orders,
            newOrder,
            fetchOrders,
            createOrder,
            deleteOrder
        };
    }
}).mount('#app'); // 將 Vue 掛載到 id="app" 的 HTML 元素上
