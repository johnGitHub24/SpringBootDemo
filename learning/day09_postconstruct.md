# Day 9 - Bean 的初始化 - @PostConstruct

有時候，我們希望在 Bean 被建立好、依賴注入完成後，自動執行一些初始化的操作。

## 1. 為什麼不用建構子 (Constructor)？
在建構子執行時，`@Autowired` 的變數還沒有被注入進來，此時呼叫這些變數會導致 `NullPointerException`。

## 2. 使用 `@PostConstruct`
標註了 `@PostConstruct` 的方法，會在 Bean 的 DI 完成後緊接著執行。

### 範例：MyBean.java (更新)
```java
@Component
public class MyBean {

    @Autowired
    private OtherBean otherBean;

    @PostConstruct
    public void init() {
        // DI 完成後，執行初始化邏輯
        System.out.println("MyBean 已完成初始化，otherBean 也可以使用了！");
    }
}
```

## 3. 執行順序
1.  **建構子 (Constructor)** 執行。
2.  **依賴注入 (DI)** 執行 (`@Autowired`)。
3.  **@PostConstruct** 執行。

---
[最後來看設定檔的應用：Day 10 - 讀取設定檔 @Value]
