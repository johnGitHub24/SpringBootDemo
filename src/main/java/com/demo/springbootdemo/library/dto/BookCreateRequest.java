package com.demo.springbootdemo.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 【職責】承載新增／更新圖書的客戶端輸入與格式驗證。
 * <p>【技巧】以 Bean Validation 在進入 Service 前擋下空白與過長欄位。
 * <p>【概念】請求 DTO 只含客戶端可寫欄位；主鍵與借閱狀態由系統維護，避免被竄改。
 * <p>【邊界】不含主鍵、借閱狀態。
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
