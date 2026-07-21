/**
 * SpringBoot Demo API & Service Verifier
 * 驗證後端 API 功能與進階微服務架構是否生效
 */
const http = require('http');

const BASE_URL = 'http://localhost:8080';

async function test(name, fn) {
  try {
    await fn();
    console.log(`✅ [PASS] ${name}`);
  } catch (err) {
    console.error(`❌ [FAIL] ${name}: ${err.message}`);
    process.exit(1);
  }
}

function request(path, method = 'GET') {
  return new Promise((resolve, reject) => {
    const req = http.request(`${BASE_URL}${path}`, { method }, (res) => {
      let data = '';
      res.on('data', chunk => data += chunk);
      res.on('end', () => resolve({ status: res.statusCode, data }));
    });
    req.on('error', reject);
    req.end();
  });
}

async function run() {
  console.log('🚀 開始 SpringBoot Demo 後端全端驗證...\n');

  // 1. API 基礎通訊驗證：健康檢查 (Orders)
  await test('API：Orders 基礎健康檢查', async () => {
    const res = await request('/api/orders');
    if (res.status !== 200) throw new Error(`預期 200 但收到 ${res.status}`);
    const json = JSON.parse(res.data);
    if (!Array.isArray(json)) throw new Error('API 應傳回訂單陣列');
  });

  // 2. 現代語法 (Java 21)
  await test('功能：Java 21 Virtual Threads & Pattern Matching', async () => {
    const res = await request('/api/test/java21');
    if (res.status !== 200) throw new Error(`預期 200 但收到 ${res.status}`);
    const json = JSON.parse(res.data);
    if (json.status !== 'Success') throw new Error(`測試端點回報失敗: ${json.error || '不明錯誤'}`);
  });

  // 3. 中介軟體 (Kafka)
  await test('功能：Kafka 訊息佇列驗證', async () => {
    const res = await request('/api/test/kafka/verify');
    if (res.status !== 200) throw new Error(`預期 200 但收到 ${res.status}`);
    const json = JSON.parse(res.data);
    if (json.status !== 'Active') throw new Error(`Kafka 端點回報未啟動或異常`);
  });

  // 4. API 通訊驗證：無效路徑處理
  await test('API：無效路徑應回傳 404', async () => {
    const res = await request('/api/non_existent_path');
    if (res.status !== 404) throw new Error(`預期 404 但收到 ${res.status}`);
  });

  console.log('\n✨ 後端驗證全部通過！系統架構穩健。');
}

run();
