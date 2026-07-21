package com.demo.springbootdemo.library.dto;

/**
 * 【職責】作為圖書對外回應的展示用資料契約。
 * 【技巧】以純 POJO 承載欄位，與 JPA 實體解耦。
 * 【概念】API／gRPC 回傳 DTO 可避免懶加載與持久化細節外洩；寫入請用 {@link BookCreateRequest}。
 * 【邊界】不含持久化映射或請求驗證。
 */
public class BookDto {
    private Integer id;
    private String title;
    private String author;
    private String category;
    private Boolean isBorrowed;
    private String borrowerName;

    // --- Getters and Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Boolean getIsBorrowed() { return isBorrowed; }
    public void setIsBorrowed(Boolean borrowed) { isBorrowed = borrowed; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
}
