package com.demo.api;

import com.demo.springbootdemo.SpringBootDemoApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 【職責】Library REST 契約整合測試：真實 {@link com.demo.springbootdemo.library.service.BookService} + H2。
 * 【技巧】先 POST 建書再打 GET／borrow；錯誤路徑走全域 {@code BookNotFoundException} → 404。
 * 【概念】與 {@code BookControllerTest}／{@code BookServiceTest} 共用 CASE-BOOK-* Acceptance。
 */
@SpringBootTest(classes = SpringBootDemoApplication.class, properties = {
        "seata.enabled=false",
        "spring.cache.type=simple"
})
@AutoConfigureMockMvc
class BookApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * CASE-BOOK-CTL-001：取得書籍成功。
     * Given: 已建立圖書；When: GET /library/books/{id}；Then: 200 + title/id 正確。
     */
    @Test
    void getBook_success() throws Exception {
        int id = createBook("Spring 整合測試", "Demo Author");

        mockMvc.perform(get("/library/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Spring 整合測試"));
    }

    /**
     * CASE-BOOK-CTL-002：書籍不存在回 404。
     * Given: 無此 id；When: GET /library/books/999999；Then: 404 + 錯誤訊息。
     */
    @Test
    void getBook_notFound() throws Exception {
        mockMvc.perform(get("/library/books/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("找不到書籍，ID: 999999"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    /**
     * CASE-BOOK-SVC-001：借書成功。
     * Given: 未借出圖書；When: POST /library/books/{id}/borrow；Then: 200、isBorrowed=true。
     */
    @Test
    void borrowBook_success() throws Exception {
        int id = createBook("可借圖書", "Author A");

        mockMvc.perform(post("/library/books/" + id + "/borrow").param("borrower", "使用者A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isBorrowed").value(true))
                .andExpect(jsonPath("$.borrowerName").value("使用者A"));
    }

    /**
     * CASE-BOOK-SVC-002：借書失敗（已借出）。
     * Given: 已借出圖書；When: 再次 borrow；Then: 404（示範例外語意）。
     */
    @Test
    void borrowBook_alreadyBorrowed() throws Exception {
        int id = createBook("已借圖書", "Author B");
        mockMvc.perform(post("/library/books/" + id + "/borrow").param("borrower", "別人"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/library/books/" + id + "/borrow").param("borrower", "我"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("已經被借走")));
    }

    private int createBook(String title, String author) throws Exception {
        String body = """
                {"title":"%s","author":"%s","category":"demo"}
                """.formatted(title, author);
        MvcResult result = mockMvc.perform(post("/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asInt();
    }
}
