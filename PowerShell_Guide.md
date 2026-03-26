# PowerShell 測試腳本執行指南 (test-all.ps1)

本文件將引導你如何執行 `test-all.ps1` 腳本，並介紹相關的基本操作與語法。

---

## 1. 執行前準備

在使用此腳本之前，請確保以下事項：
1.  **啟動後端服務**：你的 Spring Boot 應用程式（`SpringBootDemo`）必須已經啟動，且預設在 `http://localhost:8080` 運行。
2.  **開啟 PowerShell**：
    *   在 Windows 搜尋列輸入 `PowerShell` 並打開。
    *   或是直接在 VS Code 的終端機（Terminal）視窗中切換到 PowerShell。

## 2. 如何執行腳本

在專案根目錄下，執行以下指令：

```powershell
./test-all.ps1
```

> [!IMPORTANT]
> **如果遇到權限錯誤 (Execution Policy Error)：**
> Windows 預設可能禁止執行本機腳本。若出現「無法載入檔案...因為在這個系統上禁止執行指令碼」的訊息，請執行以下指令解鎖權限（僅需執行一次）：
> ```powershell
> Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
> ```

## 3. 腳本內容解析

此腳本主要使用 `Invoke-RestMethod` 來模擬 HTTP 請求，以下是常見操作：

### 查詢資料 (GET)
```powershell
$orders = Invoke-RestMethod -Uri "http://localhost:8080/api/orders"
```
*   這會呼叫 API 並將回傳的 JSON 自動轉成物件儲存在 `$orders`。

### 建立資料 (POST)
```powershell
$newOrder = @{ customerName = "測試" } | ConvertTo-Json
$created = Invoke-RestMethod -Method POST -Uri "..." -ContentType "application/json" -Body $newOrder
```
*   `@{ ... }` 建立一個鍵值對資料。
*   `ConvertTo-Json` 將資料轉換成 JSON 字串。
*   `-Body` 指定要發送的資料。

### 更新與刪除 (PUT/DELETE)
*   腳本中分別使用了 `-Method PUT` 與 `-Method DELETE` 參數來指定對應的 REST 方法。

## 4. 常用 PowerShell 快捷技巧

*   **清空終端機**：輸入 `cls` 或 `clear`。
*   **查看變數值**：直接輸入帶有 `$` 的變數名稱，例如 `$orderId`，然後按 Enter。
*   **停止執行**：如果腳本卡住，按 `Ctrl + C` 強制停止。
*   **指令補全**：輸入指令或檔名開頭後按 `Tab` 鍵，PowerShell 會自動幫你補齊名稱。

---

## 5. 為什麼使用 PowerShell 進行測試？
1.  **不需安裝額外工具**：不像 Postman 需要開啟介面，或者是安裝 `curl`。
2.  **可與 CI/CD 整合**：這種腳本可以很容易地放在自動化流程中執行。
3.  **語法直觀**：對於開發者來說，其物件化的處理方式比純字串處理的 `curl` 更容易開發與維護。
