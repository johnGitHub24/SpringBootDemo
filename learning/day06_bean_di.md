# Day 6 - IoC、DI、Bean 的介紹

昨天我們認識了 IoC (控制反轉)，今天來釐清三個關鍵名詞：

## 1. Bean
在 Spring 的世界裡，由 Spring 容器管理的所有物件，我們都稱為 **Bean**。

## 2. IoC Container (IoC 容器)
負責管理 Bean 的「箱子」。它負責實例化 (Instantiate)、配置 (Configure) 以及組裝 (Assemble) 這些 Bean。

## 3. DI (Dependency Injection, 依賴注入)
這就是 IoC 的「具體實作方法」。Spring 容器透過 DI，將需要的依賴物件（Bean）注入到目標物件中。

### 生活化比喻：
*   **IoC 容器**：像是一間全自動廚房。
*   **Bean**：廚房裡的工具或食材（如：平底鍋、雞蛋）。
*   **DI**：當廚師（程式邏輯）需要煎蛋時，廚房會自動把「平底鍋」和「雞蛋」放到廚師手邊，而不需要廚師自己跑去倉庫找。

---
[明天我們將學習實作：Day 7 - Bean 的創建和注入]
