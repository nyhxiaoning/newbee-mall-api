CREATE TABLE books (
                       book_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 图书ID，主键，自增
                       title VARCHAR(255) NOT NULL,                -- 图书标题
                       author VARCHAR(255) NOT NULL,               -- 作者
                       price DECIMAL(10, 2) NOT NULL,              -- 价格
                       stock INT NOT NULL,                         -- 库存
                       description TEXT                            -- 图书描述（可选字段）
);
