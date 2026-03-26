# Day 7 - Bean 的創建和注入 - @Component、@Autowired

今天我們要學習如何實作 DI！

## 1. 創立 Bean：使用 `@Component`
只要在類別（Class）上方加上 `@Component`，Spring 就會自動把這個類別註冊為一個 Bean。

### 範例：MyBean.java
```java
package com.demo.springbootdemo;

import org.springframework.stereotype.Component;

@Component
public class MyBean {
    public void printMessage() {
        System.out.println("哈囉！我是從 Spring 容器拿出來的 Bean！");
    }
}
```

## 2. 注入 Bean：使用 `@Autowired`
當你有一個類別需要使用到 `MyBean` 時，你不需要 `new` 它，只要使用 `@Autowired`。

### 範例：MyController.java (更新)
```java
package com.demo.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired // Spring 會自動把 MyBean 注入進來
    private MyBean myBean;

    @RequestMapping("/test-bean")
    public String test() {
        myBean.printMessage();
        return "請看 IntelliJ 的 Console 輸出！";
    }
}
```

## 3. 測試
1. 啟動專案。
2. 訪問 `http://localhost:8080/test-bean`。
3. 查看 IDE 的下方的 `Run` 視窗 (Console)，你會看到：`哈囉！我是從 Spring 容器拿出來的 Bean！`

---
[如果同一個介面有兩個 Bean 該怎麼辦？請看 Day 8 - @Qualifier]
