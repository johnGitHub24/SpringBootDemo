# Java 21 深度解析：核心特性與運作原理

本文件詳細解釋 Java 21 關鍵特性的實作細節與底層邏輯，幫助您從「會用」提升到「理解」。

---

## 1. 虛擬線程 (Virtual Threads) 的奧祕

### 傳統線程 vs. 虛擬線程
- **傳統線程 (Platform Threads)：** 是作業系統 (OS) 線程的封裝。每建立一個線程，OS 就會分配一個固定的棧空間 (通常約 1MB)。由於數量受 OS 限制，無法支撐數百萬個並發任務。
- **虛擬線程：** 是運行在「載體線程」(Carrier Thread) 之上的輕量級實體。它們由 JVM 調度，而非 OS 調度。棧空間動態增長，僅在需要時分配。

### 運作原理：掛起與恢復
當虛擬線程執行阻塞式 I/O 操作 (如讀取文件、訪問資料庫) 時：
1. **掛起：** JVM 會將虛擬線程的數據存儲到堆 (Heap) 記憶體中，並釋放底層的載體線程。
2. **切換：** 載體線程現在可以去執行其他虛擬線程。
3. **恢復：** 當 I/O 完成，JVM 會重新將虛擬線程安排到合適的載體線程上繼續執行。

**具體場景解釋：** 這就是為什麼我們可以使用 `Executors.newVirtualThreadPerTaskExecutor()`。即使你開了 10,000 個虛擬線程，實際上作業系統可能只用了少量 CPU 核心對應的線程在支撐。

---

## 2. 模式匹配 (Pattern Matching) 的進階演進

### 為什麼需要 Record Patterns？
在 Java 21 之前，如果你要從一個嵌套的 Record 中取值，你需要多層 `instanceof` 或顯式調用 Getter：
```java
if (obj instanceof ColoredPoint cp) {
    int x = cp.point().x(); // 繁瑣
}
```
Java 21 引入了 **解構成員 (Destructuring)**，直接在模式中提取變數：
```java
if (obj instanceof ColoredPoint(Point(int x, int y), String color)) {
    // x, y, color 直接可用
}
```

### Guarded Patterns (`when` 子句)
這解決了 switch case 中需要額外 `if` 判定的痛點。您可以將邏輯判定直接寫在 `case` 中：
```java
case String s when s.length() > 10 -> "長字串";
```

---

## 3. Sequenced Collections 的統一性
在 Java 21 之前，獲取最後一個元素在不同集合中方式不一：
- `List`: `list.get(list.size() - 1)`
- `Deque`: `deque.getLast()`
- `SortedSet`: `set.last()`

現在透過 `SequencedCollection` 接口，所有的有序集合都支持統一的 `getFirst()`, `getLast()`, `reversed()`。這不僅簡化了 API，更提升了多態性 (Polymorphism) 的廣度。
