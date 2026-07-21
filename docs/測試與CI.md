# SpringBootDemo 測試與 CI

> Case／DoD 摘要見 [testing.md](testing.md)；權威 [SpringBootDemo-SPEC.md](../SpringBootDemo-SPEC.md)。

## 驗證指令速查

| 指令 | 用途 | 需後端運行 |
|------|------|-----------|
| `.\gradlew.bat check` | 單元／整合測試（主驗收） | 否 |
| `.\gradlew.bat test` | 測試任務 | 否 |
| `.\gradlew.bat bootRun` | 啟動後端 :8080 | — |
| 瀏覽器開啟 `test/runner.html` | 前端 suite（需 API） | 是 |

## Gradle

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\gradlew.bat clean check
.\gradlew.bat bootRun
```

報告：`build/reports/tests/`

## 三層分工

| 層 | 測什麼 | 工具 |
|----|--------|------|
| 單元／MockMvc | Controller／Service／interview 範例 | JUnit 5、MockMvc |
| 進階 | Kafka／Gateway／TCC | 部分需 Docker |
| Smoke／瀏覽器 | 真實 HTTP CRUD | `test/suite.js` |

## CI 建議

```yaml
- uses: actions/setup-java@v4
  with: { distribution: temurin, java-version: '21' }
- run: ./gradlew check
```
