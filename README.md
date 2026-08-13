# SpringBootDemo

訂單 CRUD 示範（Spring Boot 3 · Java 21 · H2），可選 Redis／Kafka，附 Vue 前端與瀏覽器測試。  
示範用途：**排除** `SecurityAutoConfiguration`（無登入牆）。

## 文件入口

單一入口：本 README。衝突以主規格為準。

| 文件 | 說明 |
|------|------|
| [SpringBootDemo-SPEC.md](SpringBootDemo-SPEC.md) | **主規格（權威）** |
| [docs/architecture.md](docs/architecture.md) | 分層與模組 |
| [docs/codeGraphic.html](docs/codeGraphic.html) | 架構圖（非權威） |
| [docs/testing.md](docs/testing.md) | 測試／Case／check |
| [docs/資料庫設計.md](docs/資料庫設計.md) | 資料庫 |
| [使用手冊.md](使用手冊.md) | 操作手冊 |
| [CLAUDE.md](CLAUDE.md) | AI 薄規則 |
| [scripts/README.md](scripts/README.md) | 驗證／啟動腳本 |

## 快速開始

### 驗證

```powershell
.\scripts\check.ps1
```

（內部載入 JDK 21 後跑 `gradlew check`。已設定 `JAVA_HOME` 時也可 `.\gradlew.bat check`。）

### 本機 Demo

IntelliJ：開本專案根目錄 → SDK 21 → Gradle Sync → **Gradle `bootRun`**（**不要**對 `SpringBootDemoApplication` 按綠色箭頭；Windows 易 native crash）。

終端：

```powershell
.\gradlew.bat bootRun
# API: http://localhost:8080/api/orders
# Swagger: http://localhost:8080/swagger-ui.html
# H2:  http://localhost:8080/h2-console
```

可選基礎設施：`docker compose up -d`（Redis／Kafka）。未啟動時 CRUD 仍可用（Kafka 發送失敗不回滾）。

Docs standard: EngineeringOS `knowledge/documentation.md`

