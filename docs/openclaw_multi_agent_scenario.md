# OpenClaw 實戰場景：多代理人協作 (資訊研究員 + 技術作家)

本文件詳細展示如何在 OpenClaw 中配置兩個具備不同功能與性格的代理人，並讓他們協同完成一項任務。

---

## 1. 代理人設定 (Soul Definitions)

在 OpenClaw 中，代理人的靈魂決定了他們如何處理信息。

### 研究員代理人 (Researcher)
- **路徑：** `agents/researcher/SOUL.md`
- **職責：** 使用搜尋工具查詢最新資訊，並整理成結構化事實清單。
- **特質：** 嚴謹、簡潔、只在乎事實。

### 作家代理人 (Writer)
- **路徑：** `agents/writer/SOUL.md`
- **職責：** 接收研究員的事實清單，將其擴展為引人入勝的技術部落格。
- **特質：** 富有表現力、注重排版、語言流暢。

---

## 2. 協作流程詳解 (The Multi-Agent Workflow)

### 步驟 A：外部消息觸發
1.  用戶透過 Discord 發送消息：「幫我寫一篇關於 OpenClaw 安全性的分析文章。」
2.  **Gateway** 接收消息並識別出這是一個複雜任務。

### 步驟 B：大腦決策 (Brain Reasoning)
**Brain** 分析任務後，啟動協作鏈結：
1.  **任務分配：** Brain 首先將需求發送給 `Researcher`。
2.  **內部通信：** `Researcher` 調用網路搜索工具 (Search Skill)，獲取安全性相關漏洞與防護機制，產出「安全性事實.md」。
3.  **接棒：** Brain 獲取「安全性事實.md」，並將其作為上下文發送給 `Writer`。

### 步驟 C：最終產出
`Writer` 接收事實後，根據其靈魂設定，將其轉化為繁體中文部落格。

---

## 3. OpenClaw 核心機制詳解

### 消息路由與權限管理 (Routing & Permissions)
OpenClaw 具備高度安全性管控：
- **Namespace 隔絕：** 每個代理人的記憶庫 (Memory) 是預設隔絕的。除非 Brain 明確將信息傳遞，否則 `Writer` 看不到 `Researcher` 的私有記憶。
- **Skill 權限限制：** 你可以設定只有 `Researcher` 能訪問 `Search_Plugin`，而 `Writer` 只能訪問 `File_System_Plugin`。這能有效防止 LLM 越權操作。

### 為什麼這比單一機器人強大？
1.  **減少衝突：** 單一機器人在處理長文本且需同時兼顧「搜尋事實」與「創意寫作」時，容易發生風格紊亂或資訊丟失。
2.  **並行處理：** 當 `Writer` 在撰寫第一章時，`Researcher` 可以已經在為第二章尋找素材。
3.  **可擴展性：** 未來你可以輕易加入一個 `Validator` 代理人，專門檢查 `Writer` 的文章是否有錯誤。
