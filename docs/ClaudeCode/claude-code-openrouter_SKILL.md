---
name: claude-code-openrouter
description: 使用 OpenRouter 免費模型啟動 Claude Code 的完整設定流程（Windows PowerShell）。當使用者想要免費使用 Claude Code、想用 OpenRouter 替代 Anthropic API、詢問如何在沒有付費訂閱的情況下使用 Claude Code、或遇到模型設定錯誤時，請務必使用此 skill。
---

# 終端機 A — 啟動 proxy
cd free-claude-code
uv run server.py

# 終端機 B — 啟動 Claude Code
$env:ANTHROPIC_BASE_URL = "http://localhost:8082"
$env:ANTHROPIC_AUTH_TOKEN = "freecc"
$env:CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC = "1"
Remove-Item Env:ANTHROPIC_API_KEY -ErrorAction SilentlyContinue
claude


# Claude Code + OpenRouter 免費方案設定（Windows PowerShell）

## 概覽

Claude Code 官方需要 Anthropic 訂閱或 API Key，但可透過 **free-claude-code proxy** 搭配 OpenRouter 免費模型來繞過此限制，完全不需要儲值。

---

## 前置需求

- Node.js（用於安裝 Claude Code）
- Python + pip（用於 proxy server）
- Git
- OpenRouter 帳號與 API Key（免費，不需信用卡）

---

## 步驟一：取得 OpenRouter API Key

1. 前往 [openrouter.ai](https://openrouter.ai) 註冊帳號
2. 進入 [openrouter.ai/settings/keys](https://openrouter.ai/settings/keys)
3. 點選 **Create Key**，複製 `sk-or-...` 格式的 key

> ⚠️ **重要：不要把 API Key 貼到公開對話或文件中！**

---

## 步驟二：安裝舊版 Claude Code（v1.0.51）

新版 Claude Code（v2.x）登入機制嚴格，不支援第三方 provider，請安裝舊版：

```powershell
npm uninstall -g @anthropic-ai/claude-code
npm install -g @anthropic-ai/claude-code@1.0.51
```

---

## 步驟三：安裝並設定 free-claude-code proxy

```powershell
# 安裝 Python uv 套件管理工具
pip install uv

# 下載 proxy 專案
git clone https://github.com/Alishahryar1/free-claude-code.git
cd free-claude-code

# 建立設定檔
copy .env.example .env

# 開啟設定檔
notepad .env
```

在記事本中填入以下內容後存檔：

```
OPENROUTER_API_KEY="sk-or-你的key"
MODEL_OPUS="open_router/deepseek/deepseek-r1-0528:free"
MODEL_SONNET="open_router/stepfun/step-3.5-flash:free"
MODEL_HAIKU="open_router/stepfun/step-3.5-flash:free"
MODEL="open_router/stepfun/step-3.5-flash:free"
```

> 💡 可至 [openrouter.ai/models?q=:free](https://openrouter.ai/models?q=:free) 查詢目前可用的免費模型

---

## 步驟四：啟動 proxy server

**開一個 PowerShell 視窗專門跑 proxy（保持開著）：**

```powershell
cd free-claude-code
uv run server.py
```

看到 `Running on http://localhost:8082` 代表成功。

---

## 步驟五：啟動 Claude Code

**再開一個新的 PowerShell 視窗：**

```powershell
$env:ANTHROPIC_BASE_URL = "http://localhost:8082"
$env:ANTHROPIC_AUTH_TOKEN = "freecc"
$env:CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC = "1"
Remove-Item Env:ANTHROPIC_API_KEY -ErrorAction SilentlyContinue

claude
```

出現提示時選 **1. Yes** 使用偵測到的 API key。

---

## 步驟六：確認運作正常

進入 Claude Code 後執行：

```
/init
```

若成功建立 `CLAUDE.md` 檔案，代表設定完成！

---

## 常見問題排解

| 錯誤訊息 | 原因 | 解法 |
|---|---|---|
| `OPENROUTER_API_KEY is not set` | `.env` 未填入 key | 重新編輯 `.env` 並重啟 proxy |
| `There's an issue with the selected model` | 模型不支援 tool calling 或已下架 | 換其他免費模型 |
| `Auth conflict` | 同時設了兩個 key 變數 | 執行 `Remove-Item Env:ANTHROPIC_API_KEY` |
| 停在瀏覽器登入畫面 | 版本太新（v2.x） | 降版到 v1.0.51 |
| `Could not load credentials` | 環境變數未正確設定 | 確認 `echo $env:ANTHROPIC_AUTH_TOKEN` 有值 |

---

## 永久設定（不用每次重貼）

將以下內容加入 PowerShell 設定檔：

```powershell
notepad $PROFILE
```

貼入：

```powershell
# Claude Code + OpenRouter
$env:ANTHROPIC_BASE_URL = "http://localhost:8082"
$env:ANTHROPIC_AUTH_TOKEN = "freecc"
$env:CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC = "1"
```

> 注意：每次使用前仍需先啟動 proxy server（步驟四）

---

## 每次使用的標準流程

1. **終端機 A**：`cd free-claude-code && uv run server.py`
2. **終端機 B**：設定環境變數 → `claude`
3. 進入 Claude Code，開始使用中文或英文指令

---

## 推薦免費模型（需支援 Tool Calling）

查詢最新清單：[openrouter.ai/models?q=:free](https://openrouter.ai/models?q=:free)

常用且穩定的選擇：
- `stepfun/step-3.5-flash:free`
- `deepseek/deepseek-r1-0528:free`
- `meta-llama/llama-3.3-70b-instruct:free`
