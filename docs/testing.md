# Testing and Verification — SpringBootDemo

> 衝突以 [SpringBootDemo-SPEC.md](../SpringBootDemo-SPEC.md) 為準。  
> 規範：EngineeringOS `knowledge/testing.md`（單元 ↔ 整合成對）

## Check command

```powershell
.\scripts\check.ps1
```

（載入 JDK 21 後執行 `gradlew check`＝本專案 `src/test` 全部 JUnit，含 `/api/`、`/integration/` 路徑的 HTTP／訊息整合。）

## Test layers

| Layer | 路徑慣例 | 說明 |
|-------|----------|------|
| 單元 | `src/test/java`（非 `/api/`、`/integration/`） | Service Mock、Controller MockMvc、面試／Java21 範例 |
| 整合 | `src/test/java/com/demo/api/`、`.../integration/` | 真實 Service + H2；Gateway 轉發；OpenAPI；EmbeddedKafka |
| 瀏覽器（可選） | `test/suite.js` + `test/runner.html` | 需後端 `bootRun` |

掃描器：`EngineeringOS/eos-minimal/hooks/scan-paired-tests.ps1`（`/api/` 視為整合層）。

## 成對 Case（單元 ↔ 整合）

同一 Acceptance、同一 hyphenated ID：

| Case | 單元 | 整合 |
|------|------|------|
| CASE-ORDER-001 | `OrderServiceTest`／`OrderControllerTest` 建立 PENDING | `OrderApiIntegrationTest` POST+GET 列表 |
| CASE-ORDER-002 | Service empty／Controller 404 | `GET /api/orders/{id}` → 404 |
| CASE-BOOK-CTL-001 | `BookControllerTest` Mock 200 | `BookApiIntegrationTest` 建書後 GET 200 |
| CASE-BOOK-CTL-002 | Mock 404 | GET 不存在 id → 404 |
| CASE-BOOK-SVC-001 | `BookServiceTest` borrow 成功 | POST `/library/books/{id}/borrow` 200 |
| CASE-BOOK-SVC-002 | 已借出拋例外 | 二次 borrow → 404 |
| CASE-GW-001 | `GatewayConfigTest` 用戶路徑常數 | `GatewayApiIntegrationTest` `/get-users/123` |
| CASE-GW-002 | 訂單路徑常數 | `/get-orders/999` |
| CASE-OPENAPI-001 | `OpenApiConfigTest` Bean title | `GET /v3/api-docs` |
| CASE-KAFKA-INT-001 | `KafkaProducerServiceUnitTest` Mock send | `KafkaBrokerIntegrationTest` EmbeddedKafka |

## 單層（刻意僅單元）Case

下列為面試／語法／訊息壓力／TCC 示範，**無對應 HTTP API**，不強制整合層：  
`CASE-IV-*`（JAVA／CORE／CLOUD／PERS／MSG／WEB）、`CASE-J21-*`、`CASE-KAFKA-PROD-001`、`CASE-KAFKA-HC-001`、`CASE-TCC-001`。

## Minimum case types

| Type | Requirement |
|------|-------------|
| Happy Path | CASE-ORDER-001、CASE-BOOK-CTL-001、CASE-GW-001／002、CASE-OPENAPI-001 |
| Error Path | CASE-ORDER-002 404；CASE-BOOK-CTL-002／CASE-BOOK-SVC-002 404 |
| Infra optional | Kafka／Redis 未啟動時 Order CRUD 仍可用（send 失敗不回滾；測試用 simple cache） |

## DoD

- [x] `.\scripts\check.ps1` 全綠（unit + HTTP／訊息整合）
- [x] 公開 Service（Order／Book）各 ≥1 單元測
- [x] REST Happy + ≥1 錯誤整合（Order、Book）
- [ ] 瀏覽器 suite（本機 `bootRun` 後可選）
