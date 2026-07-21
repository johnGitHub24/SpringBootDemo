package com.demo.springbootdemo.library.dao;

import com.demo.springbootdemo.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【職責】以 JDBC 實作圖書表的查詢與寫入。
 * 【技巧】透過 {@link NamedParameterJdbcTemplate} 與具名參數操作 {@code book} 表，避免字串拼接。
 * 【概念】手寫 SQL 可精確控制語句；相較 JPA 更接近資料庫，但缺少自動映射與分頁便利。
 * 【邊界】不含商業規則；參數皆具名綁定以防 SQL Injection。
 */
@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getBooks() {
        // 查詢所有欄位，包含借閱狀態
        String sql = "SELECT id, title, author, category, is_borrowed, borrower_name FROM book";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Book getBookById(Integer id) {
        String sql = "SELECT id, title, author, category, is_borrowed, borrower_name FROM book WHERE id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<Book> list = jdbcTemplate.query(sql, map, new BookRowMapper());
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createBook(Book book) {
        // 新增書籍時，預設借閱狀態為 false
        String sql = "INSERT INTO book(title, author, category, is_borrowed) VALUES (:title, :author, :category, :is_borrowed)";
        Map<String, Object> map = new HashMap<>();
        map.put("title", book.getTitle());
        map.put("author", book.getAuthor());
        map.put("category", book.getCategory());
        map.put("is_borrowed", false);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBook(Integer id, Book book) {
        String sql = "UPDATE book SET title = :title, author = :author, category = :category WHERE id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", book.getTitle());
        map.put("author", book.getAuthor());
        map.put("category", book.getCategory());
        jdbcTemplate.update(sql, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBook(Integer id) {
        String sql = "DELETE FROM book WHERE id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        jdbcTemplate.update(sql, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorrowStatus(Integer id, Boolean isBorrowed, String borrowerName) {
        // 專門用於更新借閱狀態的 SQL
        String sql = "UPDATE book SET is_borrowed = :is_borrowed, borrower_name = :borrower_name WHERE id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("is_borrowed", isBorrowed);
        map.put("borrower_name", borrowerName);
        jdbcTemplate.update(sql, map);
    }

    /**
     * BookRowMapper: 將資料庫結果集 (ResultSet) 轉換為 Java 物件 (Book)
     */
    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setCategory(rs.getString("category"));
            book.setIsBorrowed(rs.getBoolean("is_borrowed"));
            book.setBorrowerName(rs.getString("borrower_name"));
            return book;
        }
    }
}
