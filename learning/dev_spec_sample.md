# 開發規格書範例 (初學者入門)

這份文件旨在展示如何撰寫一份清晰、易懂的開發規格書 (Functional Specification)。對於初學者來說，定義清楚「要做什麼」比「怎麼寫程式」更重要。

---

## 1. 專案名稱：會員管理系統 (Member Management System)

### 1.1 背景說明 (Background)
為了管理圖書館的借閱，我們需要一個簡單的會員系統來儲存使用者的基本資訊與權限。

### 1.2 目標 (Objective)
實作「建立會員」與「查詢會員」兩個核心功能。

---

## 2. 功能需求 (Functional Requirements)

### 2.1 建立會員 (Create Member)
*   **欄位說明**：
    *   `memberId` (Integer): 系統自動生成 (Primary Key)。
    *   `name` (String): 會員姓名，不可為空。
    *   `email` (String): 會員信箱，格式需正確且不可重複。
*   **行為描述**：
    *   接收來自前端的 JSON 數據。
    *   驗證 Email 是否已存在。
    *   存入資料庫後回傳完整會員物件（含 `memberId`）。

### 2.2 查詢會員資訊 (Get Member)
*   **行為描述**：根據 `memberId` 查詢單一會員的所有資訊。

---

## 3. 技術規格 (Technical Specification)

### 3.1 RESTful API 設計 (API Design)
這是目前業界最通用的溝通標準。

| 功能 | 方法 (Method) | 路徑 (URL) | 參數 (Params) | 成功回傳 (Status 200/201) |
| :--- | :--- | :--- | :--- | :--- |
| **新增會員** | **POST** | `/members` | Request Body (JSON) | `{ "id": 1, "name": "John", "email": "john@email.com" }` |
| **查詢會員** | **GET** | `/members/{id}` | PathVariable (`id`) | `{ "id": 1, "name": "John", "email": "john@email.com" }` |

> [!TIP]
> **註解說明**：
> *   `POST` 代表「新增」，`GET` 代表「查詢」。
> *   `{id}` 是一個變數，動態代入要查詢的 ID。

### 3.2 資料庫結構 (Database Schema)
使用簡單的關鍵字定義表格。

| 欄位名 | 型態 | 限制 | 註解 |
| :--- | :--- | :--- | :--- |
| `id` | INT | Auto Increment, PK | 主鍵，自動遞增 |
| `name` | VARCHAR(50) | NOT NULL | 姓名 |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | 電子郵件 (不可重複) |

---

## 4. 測試案例 (Test Cases)

撰寫程式前，先想好怎麼測試。

### 4.1 正常情況 (Happy Path)
*   **測試 A**：輸入有效的姓名與未重複的 Email，呼叫 `POST /members`。
    *   **預期結果**：回傳 HTTP 201 Created，資料庫多一筆資料。
*   **測試 B**：使用已存在的 ID 呼叫 `GET /members/1`。
    *   **預期結果**：回傳 HTTP 200 OK，顯示該會員姓名。

### 4.2 異常情況 (Edge Cases / Error Handling)
*   **測試 C**：Email 格式不正確 (例如 `john.email.com` 缺少 `@`)。
    *   **預期結果**：回傳 HTTP 400 Bad Request，提示格式錯誤。
*   **測試 D**：查詢一個不存在的 `memberId` (例如 `99999`)。
    *   **預期結果**：回傳 HTTP 404 Not Found。

---

## 5. 初學者建議
1.  **文件先行**：先寫規格、再寫測試案例、最後才寫代碼。這能避免邏輯前後矛盾。
2.  **保持簡單 (KISS Principle)**：初學者不需要考慮太複雜的架構，先求功能正確運行。
3.  **註解的重要性**：在程式碼中標註「為什麼這樣寫」，比「寫了什麼」更重要。
