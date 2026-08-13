# SpringBootDemo Specification

> **Authority contract.** Conflicts resolve to this file.  
> EOS docs standard: EngineeringOS `knowledge/documentation.md`

## 0. Document map

| File | Role |
|------|------|
| This file | Master spec (authority) |
| [README.md](README.md) | Entry |
| [使用手冊.md](使用手冊.md) | API 操作與故障排除（教學） |
| [CLAUDE.md](CLAUDE.md) | Thin AI rules（EOS） |
| [docs/architecture.md](docs/architecture.md) | Architecture |
| [docs/testing.md](docs/testing.md) | Test / DoD |
| [docs/資料庫設計.md](docs/資料庫設計.md) | DB / JPA |
| [docs/testing.md](docs/testing.md) | Check commands |

## 1. Scope

- **Purpose：** Spring Boot 訂單 CRUD 示範，附 Redis／Kafka／Library／進階模組教學。
- **Stack：** Java 21 · Spring Boot 3.2.2 · Spring MVC · JPA · H2 ·（可選）Redis、Kafka · Vue 3 ESM 前端
- **Non-goals：** 正式 JWT 授權（Security 已 exclude）、真實撮合、生產級 Gateway

## 2. Architecture

See [docs/architecture.md](docs/architecture.md).

## 3. API / Contract

來源：`OrderController`、`使用手冊.md`。Base：`http://localhost:8080`

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/orders` | 查詢全部訂單 |
| GET | `/api/orders/{id}` | 查單筆；無則 404 |
| POST | `/api/orders` | 建立（body：`OrderRequest`） |
| PUT | `/api/orders/{id}` | 更新；無則 404 |
| DELETE | `/api/orders/{id}` | 刪除 |

附屬示範（非主契約）：`BookController` → `/library/info`、`/library/books`（分頁 CRUD）。

## 4. Test DoD

- [x] `.\scripts\check.ps1` green（`gradlew check`＝unit + `/api/`／`/integration/`）
- [x] Happy + not-found：`/api/orders`（CASE-ORDER-001／002）、`/library`（CASE-BOOK-*）
- [x] Gateway／OpenAPI 成對；Kafka INT 成對
- [ ]（可選）瀏覽器 `test/suite.js` 對跑中後端通過

**單層（僅單元，無 HTTP）：** `CASE-IV-*`、`CASE-J21-*`、`CASE-KAFKA-PROD-001`、`CASE-KAFKA-HC-001`、`CASE-TCC-001`。詳見 [docs/testing.md](docs/testing.md)。

## 5. Changelog

| Date | Note |
|------|------|
| 2026-08-13 | 成對 Case：ORDER／BOOK／GW／OPENAPI／KAFKA-INT；單層 IV／J21／TCC 標註；check.ps1 |
| 2026-07-10 | Filled from Controllers／使用手冊／CLAUDE；EOS 0.1.10 docs |
| 2026-07-10 | Created from EOS documentation standard skeleton |
