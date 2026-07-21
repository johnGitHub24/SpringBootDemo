# SpringBootDemo

訂單 CRUD 示範（Spring Boot 3 · Java 21 · H2），可選 Redis／Kafka，附 Vue 前端與瀏覽器測試。

## 文件入口

| 文件 | 說明 |
|------|------|
| [SpringBootDemo-SPEC.md](SpringBootDemo-SPEC.md) | **主規格書（權威）** |
| [使用手冊.md](使用手冊.md) | API 操作、H2、故障排除 |
| [docs/architecture.md](docs/architecture.md) | 分層與模組 |
| [docs/codeGraphic.html](docs/codeGraphic.html) | Tab 式架構圖（Order／Library／進階／套件） |
| [docs/testing.md](docs/testing.md) | 測試／DoD |
| [docs/資料庫設計.md](docs/資料庫設計.md) | 表與 Entity |
| [docs/測試與CI.md](docs/測試與CI.md) | check 指令速查 |
| [CLAUDE.md](CLAUDE.md) | AI／工程薄規則（EOS 0.1.4） |

## 快速開始

```powershell
.\gradlew.bat check
.\gradlew.bat bootRun
# API: http://localhost:8080/api/orders
# H2:  http://localhost:8080/h2-console
```

可選基礎設施：`docker compose up -d`（Redis／Kafka）。

Docs standard: EngineeringOS `knowledge/documentation.md`
