# Day 4 - 第一個 Spring Boot 程式

今天我們要建立一個最簡單的「Hello World」網頁。

## 1. 建立 Controller
在 Spring Boot 中，處理網頁請求的類別稱為 `Controller`。

### 範例：MyController.java
```java
package com.demo.springbootdemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 告訴 Spring 這是一個處理 HTTP 請求的類別
public class MyController {

    @RequestMapping("/hello") // 當瀏覽器輸入 /hello 時執行此方法
    public String hello() {
        return "Hello World! 這是我的第一個 Spring Boot 程式。";
    }
}
```

## 2. 關鍵註解說明
*   `@RestController`：結合了 `@Controller` 與 `@ResponseBody`。這意味著方法回傳的字串會直接顯示在網頁上，而不是導向某個 HTML 頁面。
*   `@RequestMapping("/hello")`：定義 URL 路徑對應。

## 3. 如何運行？
1. 點擊 IDE 的 `Run` 按鈕啟動 `SpringBootDemoApplication`。
2. 開啟瀏覽器並輸入：`http://localhost:8080/hello`。

## 4. 預期結果
網頁會顯示：`Hello World! 這是我的第一個 Spring Boot 程式。`

---
[恭喜完成第一個程式！明天將進入 Day 5 - Spring IoC 簡介]
