# Day 12 - Spring AOP 的用法 - @Aspect

今天我們來寫一個簡單的切面，記錄方法的執行時間。

## 1. 範例：日誌切面 (MyAspect.java)

在使用 AOP 之前，通常需要在 `build.gradle` 加入：
`implementation 'org.springframework.boot:spring-boot-starter-aop'`

### 實作切面
```java
package com.demo.springbootdemo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    // 定義切入點：攔截所有在 com.demo.springbootdemo 下的所有方法
    @Around("execution(* com.demo.springbootdemo.*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        // 執行原本的方法
        Object result = joinPoint.proceed();
        
        long end = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature().getName() + " 執行耗時：" + (end - start) + "ms");
        
        return result;
    }
}
```

## 2. 註解說明
*   `@Aspect`：告訴 Spring 這是一個切面類別。
*   `@Around`：在方法執行前後都執行該段邏輯。
*   `execution(...)`：這就是 Pointcut，定義哪些類別的方法要被攔截。

## 3. 測試
1. 啟動專案。
2. 訪問前面寫過的 `/hello` 或 `/test-bean`。
3. 查看 Console，你會看到自動輸出的耗時記錄：`hello 執行耗時：2ms`。

---
[AOP 完成！接下來進入重頭戲：Day 13 - Spring MVC 簡介]
