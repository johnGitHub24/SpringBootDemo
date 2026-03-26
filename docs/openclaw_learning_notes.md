# OpenClaw 多代理人 (Multi-bots) 框架學習筆記

OpenClaw 是 2025-2026 年迅速崛起的一款開源 AI 自動化框架，專為構建、部署與管理**自主 AI 代理人 (Autonomous Agents)** 而設計。

---

## 1. 什麼是 OpenClaw？

與傳統的單向聊天機器人不同，OpenClaw 的核心理念是**「代理人即是操作者」**。它不僅能對話，還能持續運行、保持上下文，並在各個服務（如 WhatsApp, Discord, Slack, Local API）之間執行任務。

### 核心特性
- **模型無關 (Model Agnostic)：** 支持 GPT-4, Claude 3.5, Ollama 等多種模型。
- **自我託管 (Self-hosted)：** 可運行在 Windows, Mac 或 Linux。
- **持久化記憶：** 使用 Markdown 文件存儲本地記憶。
- **SOUL 設定系統：** 通過 `SOUL.md` 定義代理人的性格、道德準則與行動邏輯。

---

## 2. 核心架構 (Five-Component Architecture)

1.  **Gateway (網關)：** 處理不同平台（Telegram, Discord 等）的消息路由。
2.  **Brain (大腦)：** 核心推理引擎，負責 LLM 調用與決策。
3.  **Memory (記憶)：** 本地化存儲，讓代理人記得與用戶的歷史互動。
4.  **Skills (技能)：** 插件系統，允許代理人調用外部工具（如執行腳本、查詢數據庫）。
5.  **Heartbeat (心跳)：** 定時任務調度器，確保代理人能主動執行任務（例如：每早 9 點發送報告）。

---

## 3. 多代理人 (Multi-bot) 協作

在 OpenClaw 中，您可以同時運行多個代理人，每個代理人擁有獨立的 `SOUL.md` 文件：

- **Agent A (分析師)：** 負責分析數據。
- **Agent B (管理員)：** 負責協調資源。
- **Agent C (客服)：** 負責外部通訊。

這些代理人可以透過 OpenClaw 的 **Task Routing** 機制互相協作，完成更複雜的工作流。

---

## 4. 快速入門指南 (概念流程)

### 第一步：環境準備
通常需要 Node.js 環境，並克隆 OpenClaw 倉庫。

### 第二步：定義性格 (Designing a Soul)
在 `agents/my-bot/SOUL.md` 中撰寫：
```markdown
# MyBot Soul
你是一個專注於 Java 開發的技術助手，你的語氣冷靜且專業。
你只會使用繁體中文進行溝通。
你的目標是協助用戶優化代碼。
```

### 第三步：配置模型與渠道
在 `config.yaml` 中配置您的 API Key 以及通訊管道（例如 Discord Bot Token）。

### 第四步：啟動
使用命令啟動框架，監聽外部進入的消息。

---

## 5. 為什麼選擇 OpenClaw？
對於希望建立具備**主動行動力**而非僅僅是**被動回答**的 AI 系統的開發者來說，OpenClaw 是目前最靈活且社群熱度極高的選擇之一。
