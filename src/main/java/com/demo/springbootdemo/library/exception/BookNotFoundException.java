package com.demo.springbootdemo.library.exception;

/**
 * 自定義業務例外：找不到書籍
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(Integer id) {
        super("找不到書籍，ID: " + id);
    }
}
