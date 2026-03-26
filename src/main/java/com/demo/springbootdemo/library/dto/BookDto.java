package com.demo.springbootdemo.library.dto;

/**
 * 圖書顯示物件 (DTO)
 * 升級內容：過濾掉不需要直接暴露給前端的敏感資料
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
