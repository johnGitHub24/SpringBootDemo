package com.demo.springbootdemo.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 圖書實體類別 (Book Entity)
 * 升級內容：使用 JPA 註解與 Bean Validation
 */
@Entity // 宣告這是一個資料庫實體
@Table(name = "book") // 對映到資料表 "book"
public class Book {
    
    @Id // 設定為主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 設定為自動增量 (Auto-increment)
    private Integer id;
    
    @NotBlank(message = "書名不可為空")
    @Size(max = 255, message = "書名長度不可超過 255 字元")
    @Column(nullable = false) // 資料庫層級的限制
    private String title;
    
    @NotBlank(message = "作者不可為空")
    @Size(max = 100, message = "作者名稱過長")
    @Column(nullable = false)
    private String author;
    
    @Size(max = 50, message = "分類長度限制為 50")
    private String category;

    @Column(name = "is_borrowed")
    private Boolean isBorrowed = false;

    @Column(name = "borrower_name")
    private String borrowerName;

    // --- Getters and Setters (建議實務上使用 Lombok @Data) ---

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
