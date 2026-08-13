# Java 與 Spring 技術面試問題整理

本文件整理了常見的 Java、Spring、Spring Cloud、資料庫與分散式系統的技術面試問題，並提供詳細的解答與實作範例。

---

## 目錄
1. [Java 核心 (ThreadLocal, Collections, JVM, 執行緒)](#1-java-核心)
2. [Spring 核心與設計模式 (IOC, AOP, Proxy)](#2-spring-核心與設計模式)
3. [REST 與 Web 技術 (Idempotency, PUT/PATCH)](#3-rest-與-web-技術)
4. [Spring Cloud 與微服務 (Gateway, Feign, gRPC)](#4-spring-cloud-與微服務)
5. [事務管理與持久化 (Transaction, Locking, Isolation)](#5-事務管理與持久化)
6. [消息佇列 (Message Queue, Kafka, RabbitMQ)](#6-消息佇列)

---

## 1. Java 核心

### 1.1 ThreadLocal
**Q1: 通常在什麼時候需要使用 ThreadLocal？**
ThreadLocal 在多執行緒環境下，當需要確保某些變數在每個執行緒中「獨立存在」且「不共享」時非常有用。常見情境：
*   **資料庫連線**：每個執行緒擁有獨立連線，避免干擾。
*   **Session 管理**：儲存每個請求（執行緒）專屬的使用者資訊。
*   **交易管理**：確保交易上下文的隔離。
*   **格式化工具**：如 `SimpleDateFormat` 是非執行緒安全的，可用 ThreadLocal 為每個執行緒建立實例。

**Q2: 為什麼 ThreadLocal 是 thread safe？**
因為每個執行緒都有自己對應的 ThreadLocal 變數副本。每個執行緒修改的都是自己的私有變數，不需同步操作（Synchronization），從而避免競態條件（Race Condition）。

**實作範例：** `com.demo.interview.JavaCoreExamples` 中的 `ThreadLocalExample`。

### 1.2 集合框架 (ArrayList vs LinkedList)
**Q: ArrayList 與 LinkedList 在 get、add 與 remove 的時間複雜度比較？**

| 操作 | ArrayList | LinkedList | 說明 |
| :--- | :--- | :--- | :--- |
| **get(index)** | O(1) | O(n) | ArrayList 基於陣列，支援隨機存取。 |
| **add(element)** | O(1) | O(1) | 加在最後面時。 |
| **remove(index)** | O(n) | O(n) | ArrayList 需要移動後續元素；LinkedList 需要尋找節點。 |

### 1.3 JVM 記憶體管理
**Q: 說明 JVM 的分配與回收方式？**
*   **Heap (堆)**：分為 Young Generation (Eden, Survivor) 與 Old Generation。
*   **Non-Heap (非堆)**：包含 Code Cache 與 Metaspace (永久代)。
*   **回收機制**：Minor GC (針對新生代) 與 Full GC (針對整個堆)。

### 1.4 進程 (Process) 與 執行緒 (Thread)
*   **Process**：OS 分配資源的最小單位，資源不共享，切換成本高。
*   **Thread**：運算排程的最小單位，共用進程資源，切換快但需注意同步問題。

---

## 2. Spring 核心與設計模式

### 2.1 IOC (Inversion of Control)
控制反轉：依賴抽象或介面，不依賴實作。當更換實作類別時，不需修改呼叫端的程式碼。

### 2.2 AOP (Aspect-Oriented Programming)
面向切面程式設計：透過動態代理實現功能隔離（如 Log、安全、交易），降低耦合度。
*   **Aspect (切面)**：模組化的對象。
*   **Advice (通知)**：切面要執行的工作。
*   **PointCut (切入點)**：定義執行地點。

### 2.3 @Qualifier
當一個介面有多個實作時，Spring 無法判斷注入哪一個。`@Qualifier` 用於指定具體的 Bean 名稱。

### 2.4 代理模式 (Proxy Pattern)
在客戶端與目標對象之間引入中介，可增添額外服務（如日誌、權限檢查）而不修改原始對象。

---

## 3. REST 與 Web 技術

### 3.1 PUT vs PATCH
*   **PUT**：更新「全部」資料。
*   **PATCH**：更新「部分」資料。

### 3.2 冪等特性 (Idempotent)
不論執行多少次，其結果都與執行一次相同的特性。常見於 GET, HEAD, PUT, DELETE。

---

## 4. Spring Cloud 與微服務

### 4.1 Spring Cloud Gateway
API 網關服務，提供路由 (Route)、斷言 (Predicate) 與過濾器 (Filter) 功能。
核心：`Route = ID + URI + Predicates + Filters`。

### 4.2 OpenFeign
簡化 HTTP 客戶端的撰寫，只需定義介面並加上註解，Feign 會自動封裝請求細節。

### 4.3 gRPC
基於 HTTP/2，使用 Protobuf 位元組碼傳輸。支援雙向流、高效序列化。
*   **IDL (.proto)**：定義服務介面。

---

## 5. 事務管理與持久化

### 5.1 Spring 事務傳播 (Propagation)
常見類型：
*   **REQUIRED (預設)**：支援當前事務，若無則建新的。
*   **REQUIRES_NEW**：暫停當前事務，建立全新事務。
*   **NESTED**：嵌套在當前事務中執行。

### 5.2 悲觀鎖 vs 樂觀鎖
*   **悲觀鎖** (`SELECT ... FOR UPDATE`)：假設資料隨時被改，直接鎖住，安全性高但吞吐量低。
*   **樂觀鎖** (Version 欄位)：假設變動不頻繁，更新時才比對版本號，效率高但需處理衝突。

### 5.3 隔離等級 (Isolation Levels)
*   **Read Uncommitted** -> **Read Committed** -> **Repeatable Read (MySQL 預設)** -> **Serializable**。

---

## 6. 消息佇列

### 6.1 Kafka vs RabbitMQ
*   **RabbitMQ**：智慧代理（Smart Broker），訊息被取用後即刪除。
*   **Kafka**：智慧消費者（Smart Consumer），基於 Log 保留訊息，可重複消費。

### 6.2 RabbitMQ 交換器類型
*   **Direct**：完全匹配路由鍵。
*   **Topic**：模糊比對路由鍵 (# 為多詞, * 為一詞)。
*   **Fanout**：廣播到所有繫結佇列。
