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
| [docs/測試與CI.md](docs/測試與CI.md) | Check commands |

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

- [ ] `.\gradlew.bat check` green
- [ ] Happy path + not-found path covered for `/api/orders`
- [ ]（可選）瀏覽器 `test/suite.js` 對跑中後端通過

## 5. Changelog

| Date | Note |
|------|------|
| 2026-07-10 | Filled from Controllers／使用手冊／CLAUDE；EOS 0.1.4 docs |
| 2026-07-10 | Created from EOS documentation standard skeleton |
