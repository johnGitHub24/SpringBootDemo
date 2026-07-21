# Architecture — SpringBootDemo

> 衝突以 [SpringBootDemo-SPEC.md](../SpringBootDemo-SPEC.md) 為準。  
> 專案手冊：根目錄 `CLAUDE.md`（Spring Boot 四層）· EngineeringOS @ 0.1.4

## Layers

| Layer | Path | Responsibility |
|-------|------|----------------|
| Controller | `com.demo.controller`、`...library.controller`、`advanced.*` | HTTP／參數／ResponseEntity |
| Service | `com.demo.service` 等 | 商業邏輯 |
| Repository | `com.demo.repository` | JPA |
| Model／DTO | `model`／`dto` | 結構與驗證 |
| Infra demo | Redis／Kafka（compose） | 快取／事件（可選） |

## Module map

| Module | Notes |
|--------|-------|
| Orders | `OrderController` — CRUD `/api/orders` |
| Library | `BookController` — 示範模組 |
| Advanced MS | `SimulatedOrderController`／`SimulatedUserController` |
| WebSocket | `PaymentNotificationController` |
| Java 21 | `Java21FeaturesDemo` 等語法示範 |
| Gateway demo | Spring Cloud Gateway 路由示範（測試用固定 port） |
| Frontend／Test | `frontend/app.js`（Vue 3）· `test/suite.js` |

## Runtime

```text
Browser (Vue / test runner) → Spring Boot (:8080)
    → H2 (dev/test) / optional Redis + Kafka (compose)
```

## Visual maps

| 文件 | 用途 |
|------|------|
| [codeGraphic.html](codeGraphic.html) | Tab：Order／Library／進階／套件（圖為主） |
