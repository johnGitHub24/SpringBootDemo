# Claude Code Windows 安裝與配置指南

本指南將手把手教你如何在 Windows 環境下安裝 Anthropic 的 Claude Code CLI 工具，並將其整合至 VS Code，最後介紹如何串聯免費的大語言模型（LLM）代理服務。

---

## 1. 環境準備

在開始之前，請確保你的電腦已安裝以下工具：

- **Node.js (版本 18 或更高)**：Claude Code 是基於 Node.js 開發的。
    - [官方下載連結](https://nodejs.org/) (建議下載 LTS 版本)
- **Git**：用於版本控制整合。
    - [官方下載連結](https://git-scm.com/)
- **WSL (建議)**：雖然 Claude Code 支援原生的 Windows 終端機，但在 **WSL2 (Ubuntu)** 環境下執行更穩定且效能更佳。
    - 指令：`wsl --install` (於 PowerShell 管理員模式執行)

---

## 2. 安裝 Claude Code

打開你的終端機（PowerShell, Command Prompt 或 WSL），執行以下指令進行全域安裝：

```bash
npm install -g @anthropic-ai/claude-code
```

安裝完成後，可以輸入以下指令確認是否成功：

```bash
claude --version
```

---

## 3. VS Code 整合

要在 VS Code 中完美使用 Claude Code，請按照以下步驟操作：

1. **開啟整合終端機**：在 VS Code 中按下 ``Ctrl + ` ``。
2. **切換路徑**：確保你的終端機路徑位於你想讓 AI 處理的專案根目錄。
3. **啟動 Claude**：輸入 `claude` 即可進入互動模式。
4. **推薦配置**：建議將 VS Code 的預設終端機設為 Git Bash 或 WSL，以獲得更好的色彩顯示與操作體驗。

---

## 4. 串聯免費大語言模型 (透過 OpenRouter)

如果你想節省 Anthropic 官方 API 的點數，或想測試其他免費模型，可以透過 **OpenRouter** 或 **OneAPI** 作為代理。

### 步驟 A：獲取 API Key
1. 前往 [OpenRouter 官網](https://openrouter.ai/) 註冊帳號。
2. 在 Settings 中生成一個新的 **API Key**。

### 步驟 B：配置環境變數 (重要)
Claude Code 支援透過環境變數更改 API 的 Base URL。請在你的 shell 配置檔中（如 `.bashrc`, `.zshrc` 或 Windows 的環境變數設定）加入以下內容：

**Windows PowerShell:**
```powershell
$env:ANTHROPIC_BASE_URL="https://openrouter.ai/api/v1"
$env:ANTHROPIC_API_KEY="你的_OpenRouter_API_Key"
```

**WSL / Linux / Git Bash:**
```bash
export ANTHROPIC_BASE_URL="https://openrouter.ai/api/v1"
export ANTHROPIC_API_KEY="你的_OpenRouter_API_Key"
```

### 步驟 C：啟動並使用
完成配置後，再次啟動 `claude`。此時它會經由 OpenRouter 發送請求。你可以在啟動時指定模型：

```bash
# 例如使用 OpenRouter 上的免費模型
claude --model google/gemini-2.0-flash-001
```

---

## 5. 語系設定 (調整為繁體中文回應)

如果您希望 Claude Code 預設以繁體中文與您溝通，可以透過以下幾種方式設定：

### 方式 A：透過環境變數 (建議)
設定 `CLAUDE_LANGUAGE` 環境變數，這會讓工具啟動時自動了解您的偏好。

- **PowerShell:**
  ```powershell
  $env:CLAUDE_LANGUAGE="Traditional Chinese"
  ```
- **Git Bash / WSL:**
  ```bash
  export CLAUDE_LANGUAGE="Traditional Chinese"
  ```

### 方式 B：進入對話後要求
在 `claude` 的對話視窗中直接輸入一次：
> 「請從現在起使用繁體中文與我對話。」

---

## 6. 常見問題與進階用法

- **更新工具**：定期執行 `npm update -g @anthropic-ai/claude-code` 以獲取最新功能。
- **權限請求**：Claude 在執行刪除檔案或執行指令前會尋求你的許可，請務必詳細閱讀 AI 的計畫。

恭喜！你現在已經擁有一個強大的 AI 程式助手在你的 Windows 中了。
