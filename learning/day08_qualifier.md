# Day 8 - 指定注入的 Bean - @Qualifier

今天我們要解決一個常見的問題：如果有兩個同類型的 Bean，Spring 該注入哪一個？

## 1. 情境模擬 (Scenario)
假設我們有一個介面 `Printer`，兩個實作類別 `HpPrinter` 和 `CanonPrinter`。

### 範例：介面與實作
```java
public interface Printer {
    void print();
}

@Component
public class HpPrinter implements Printer {
    public void print() { System.out.println("HP Printer 正在列印..."); }
}

@Component
public class CanonPrinter implements Printer {
    public void print() { System.out.println("Canon Printer 正在列印..."); }
}
```

## 2. 解決衝突：使用 `@Qualifier`
如果你直接 `@Autowired private Printer printer;`，Spring 會因為找不到唯一的 Bean 而報錯。這時你需要 `@Qualifier`。

### 範例：MyController.java (更新)
```java
@RestController
public class MyController {

    @Autowired
    @Qualifier("hpPrinter") // 告訴 Spring 注入名為 hpPrinter 的 Bean
    private Printer printer;

    @RequestMapping("/print")
    public String testPrint() {
        printer.print();
        return "請看 Console 輸出 HP 列印結果！";
    }
}
```

> [!NOTE]
> **Bean 的預設名稱**：類別名稱首字母小寫。例如 `HpPrinter` 的 Bean 名稱就是 `hpPrinter`。

---
[明天來看 Bean 的生命週期：Day 9 - @PostConstruct]
