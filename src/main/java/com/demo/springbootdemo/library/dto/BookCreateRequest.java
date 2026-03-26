package com.demo.springbootdemo.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新增圖書請求物件 (DTO)
 * 升級內容：封裝前端發來的資料，並進行參數校驗 (Validation)
 */
public class BookCreateRequest {

    @NotBlank(message = "書名不可為空")
    @Size(max = 255, message = "書名長度上限為 255")
    private String title;

    @NotBlank(message = "作者不可為空")
    @Size(max = 100, message = "作者名稱上限為 100")
    private String author;

    private String category;

    // --- Getters and Setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
