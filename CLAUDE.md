# SpringBootDemo — 專案規則（薄）

繼承：EngineeringOS eos-minimal @ **0.1.10**  
公版：`EngineeringOS/eos-minimal/`  
權威規格：[SpringBootDemo-SPEC.md](SpringBootDemo-SPEC.md)

## 與公版差異

- Backend port: 8080
- Framework: Spring Boot 3.2 · Java 21 · JPA ·（可選）Redis／Kafka
- Security: **排除** `SecurityAutoConfiguration`（示範用）
- DB: H2 in-memory（`jdbc:h2:mem:testdb`）
- Frontend: Vue 3 ESM（`frontend/`）；瀏覽器測試 `test/suite.js`
- 驗證入口：`.\scripts\check.ps1`（載入 JDK 21 後 `gradlew check`）
- 本機 Demo：IntelliJ／Gradle `bootRun`（**勿**對 `*Application` 綠箭頭）

## 本專案專屬

- Domain: Order CRUD（`/api/orders`）、Library 示範（`/library`）、進階 Kafka／TCC 等
- 操作手冊：[使用手冊.md](使用手冊.md)
- Architecture: `docs/architecture.md` · Test: `docs/testing.md`
- Infra: `compose.yaml`（Redis + Kafka + Zookeeper）

## 註解深度
- comment_verbosity: **detailed**
- 權威：`EngineeringOS/eos-minimal/knowledge/comments.md` §0／§3b（eos-minimal @ 0.1.10）
- 結構：【職責】【技巧】【概念】；簡單 getter 可併入類別說明


## Git Remote
- 帳號：`johnGitHub24`；一專案一 repo
- 規範：`EngineeringOS/eos-minimal/knowledge/專案上船-GitHub.md`

## 回寫

問題與公版改善建議 → `EngineeringOS/eos-minimal/feedback/SYNC_LOG.md`
