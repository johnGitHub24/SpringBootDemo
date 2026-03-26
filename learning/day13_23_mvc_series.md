# Day 13 - Spring MVC 簡介

## 什麼是 MVC？
MVC 是一種軟體架構模式，將程式分成三部分：
1.  **Model (模型)**：負責資料處理與業務邏輯。
2.  **View (視圖)**：負責顯示介面。
3.  **Controller (控制器)**：負責接收請求、呼叫 Model 並決定回傳哪個 View。

在 Spring Boot 中，我們通常工作於 **Model** 和 **Controller**。

---
# Day 14 - Http 協議介紹
HTTP 是網路溝通的語言。
*   **Request (請求)**：瀏覽器發給伺服器，包含 Method (GET/POST)、URL、Header、Body。
*   **Response (回應)**：伺服器回給瀏覽器，包含 Status Code (200, 404)、Header、Body。

---
# Day 15 - Url 路徑對應 - @RequestMapping
`@RequestMapping` 用於定義 URL 與方法的對應關係。
```java
@RequestMapping("/api/v1") // 類別層級，定義基礎路徑
@RestController
public class MyMvcController {
    @RequestMapping("/welcome") // 方法層級，完整路徑為 /api/v1/welcome
    public String welcome() { return "Welcome!"; }
}
```

---
# Day 16 & 17 - JSON 格式與 @RestController
*   **JSON**：一種輕量級的資料交換格式，例如 `{"id": 1, "name": "Apple"}`。
*   **@RestController**：當你回傳一個「物件」時，Spring 會自動把它轉成 JSON 格式回傳。

---
# Day 18 - GET 和 POST
*   **GET**：用於獲取資料。參數放在 URL 後面。
*   **POST**：用於新增資料。資料放在 Request Body 裡。

---
# Day 19 & 20 - 取得請求參數
### 1. `@RequestParam` (URL 參數)
`http://localhost:8080/user?id=123`
```java
public String getUser(@RequestParam Integer id) { return "ID: " + id; }
```

### 2. `@RequestBody` (JSON 格式)
```java
public String addUser(@RequestBody User user) { return "Added: " + user.getName(); }
```

### 3. `@PathVariable` (路徑變數)
`http://localhost:8080/user/123`
```java
@GetMapping("/user/{id}")
public String getById(@PathVariable Integer id) { return "Path ID: " + id; }
```

---
# Day 21 & 22 - RESTful API 實作
使用更具語意化的註解：
*   `@GetMapping`
*   `@PostMapping`
*   `@PutMapping` (更新)
*   `@DeleteMapping` (刪除)

---
# Day 23 - Http Status Code
*   `200 OK`: 成功。
*   `201 Created`: 新增成功。
*   `400 Bad Request`: 客戶端輸入錯誤。
*   `404 Not Found`: 找不到資源。
*   `500 Internal Server Error`: 伺服器壞了。
