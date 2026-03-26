# Day 24 - Day 28: Spring JDBC 與 MVC 三層架構

## Day 24 & 25 - Spring JDBC 簡介與設定
Spring JDBC 讓我們可以用簡單的配置來操作資料庫。
在 `application.properties` 配置好 JDBC URL 後，Spring 會自動提供 `NamedParameterJdbcTemplate` 給我們使用。

## Day 26 & 27 - 執行 SQL (增刪改查)
### 範例：存取資料庫
```java
@Autowired
private NamedParameterJdbcTemplate jdbcTemplate;

// 新增
public void addMember(Member member) {
    String sql = "INSERT INTO member(name, email) VALUES (:name, :email)";
    Map<String, Object> map = new HashMap<>();
    map.put("name", member.getName());
    map.put("email", member.getEmail());
    jdbcTemplate.update(sql, map);
}

// 查詢 (使用 RowMapper)
public Member getMemberById(Integer id) {
    String sql = "SELECT id, name, email FROM member WHERE id = :id";
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    return jdbcTemplate.queryForObject(sql, map, new MemberRowMapper());
}
```

## Day 28 - MVC 三層式架構 (非常重要)
在實際開發中，我們會把程式分成三層，職責分明：
1.  **Controller 層**：負責接收讀者請求、驗證參數。
2.  **Service 層**：負責商業邏輯（例如：借書前檢查是否有逾期）。
3.  **Dao (Repository) 層**：負責與資料庫溝通 (SQL)。

---
# Day 29 - 實戰演練：圖書館系統
我們將這 30 天所學的技術整合，在 `com.demo.springbootdemo` 下建立一個簡單的 Library 專案。

### 功能：
1.  **BookController**：提供 API。
2.  **BookService**：商業邏輯。
3.  **BookDao**：操作資料庫。

---
# Day 30 - 總結
恭喜！您已經掌握了 Spring Boot 的核心：
*   **IoC/DI**：解耦與管理 Bean。
*   **MVC**：建立 RESTful API。
*   **JDBC**：操作資料庫。
*   **三層架構**：良好的程式組織結構。

未來可以繼續學習：Spring Data JPA、Spring Security、Spring Cloud 等進階技術。
