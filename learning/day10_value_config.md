# Day 10 - 讀取 Spring Boot 設定檔 - @Value、application.properties

今天我們要學習如何讀取外部設定檔中的屬性值，讓程式更有彈性。

## 1. 在設定檔增加數值
開啟 `src/main/resources/application.properties`。

### 範例：設定檔內容
```properties
my.app.name=圖書館系統
my.app.version=1.0.0
```

## 2. 使用 `@Value` 讀取
在類別中使用 `@Value("${key}")` 即可取得對應的值。

### 範例：MyController.java (更新)
```java
@RestController
public class MyController {

    @Value("${my.app.name}")
    private String appName;

    @Value("${my.app.version}")
    private String appVersion;

    @RequestMapping("/info")
    public String getInfo() {
        return "專案名稱：" + appName + "，版本：" + appVersion;
    }
}
```

## 3. 測試
1. 啟動專案。
2. 訪問 `http://localhost:8080/info`。
3. 網頁顯示：`專案名稱：圖書館系統，版本：1.0.0`。

---
[IoC 系列結束！明天進入新章節：Day 11 - Spring AOP 簡介]
