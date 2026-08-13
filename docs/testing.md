# Testing and Verification — SpringBootDemo

> 衝突以主規格為準。規範：EngineeringOS `knowledge/testing.md`

## Check command

```powershell
.\gradlew.bat test
# 或專案慣用 check（若有）
.\gradlew.bat check
```

## Test layers

| Layer | 說明 |
|-------|------|
| 單元／整合 | `src/test/java` — Order、Gateway、Java21 等 |
| 瀏覽器 | `test/suite.js` + `test/runner.html`（需後端運行） |

## Minimum case types

| Type | Requirement |
|------|-------------|
| Happy Path | 建立／查詢訂單等核心 API |
| Error Path | 驗證失敗、找不到資源 |
| Infra optional | Kafka 未啟動時 CRUD 仍可用 |

## DoD

- [x] `gradlew test` 全綠（含 Gateway／Record Patterns 修正後）
- [ ] 瀏覽器 suite（本機啟動後）
- [x] 驗證入口寫在 README
