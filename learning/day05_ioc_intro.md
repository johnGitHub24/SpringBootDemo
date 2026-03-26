# Day 5 - Spring IoC 簡介

## 什麼是 IoC (Inversion of Control)？

**IoC (控制反轉)** 是 Spring 框架最核心的概念。

### 傳統方式 (沒有使用 IoC)
當一個類別 A 需要用到類別 B 時，類別 A 會自己負責用 `new` 來建立類別 B 的實例。
```java
// 傳統：A 自己掌控 B 的生命週期
public class ClassA {
    private ClassB b = new ClassB(); 
}
```

### Spring 方式 (使用 IoC)
控制權移交給了 Spring 容器。當類別 A 需要類別 B 時，Spring 容器會負責把類別 B 的實例「交給」類別 A，類別 A 不再需要自己 `new`。

### 為什麼要這樣做？
1.  **解耦 (Decoupling)**：類別之間不再強依賴。
2.  **易於測試**：測試 A 時，可以輕易地更換 B 的實作（例如 Mock）。
3.  **集中管理**：物件的生命週期（建立、銷毀）由 Spring 統一管理。

---
[前往 Day 6 - IoC、DI、Bean 的介紹]
