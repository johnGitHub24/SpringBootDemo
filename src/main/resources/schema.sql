CREATE TABLE IF NOT EXISTS book (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    is_borrowed BOOLEAN DEFAULT FALSE, -- 借閱狀態：TRUE 為已借出，FALSE 為在館
    borrower_name VARCHAR(100),         -- 借閱者姓名
    PRIMARY KEY (id)
);

-- 插入一些初始測試數據
INSERT INTO book (title, author, category, is_borrowed) SELECT 'Spring Boot 零基礎入門', '王小明', '技術', FALSE WHERE NOT EXISTS (SELECT 1 FROM book WHERE title = 'Spring Boot 零基礎入門');
INSERT INTO book (title, author, category, is_borrowed) SELECT 'Java 程式設計指南', '李四', '技術', FALSE WHERE NOT EXISTS (SELECT 1 FROM book WHERE title = 'Java 程式設計指南');
INSERT INTO book (title, author, category, is_borrowed) SELECT 'Clean Code 敏捷軟體開發', 'Robert C. Martin', '技術', FALSE WHERE NOT EXISTS (SELECT 1 FROM book WHERE title = 'Clean Code 敏捷軟體開發');
