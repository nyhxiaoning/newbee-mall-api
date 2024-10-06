package ltd.newbee.mall.entity;

// TODO:这里使用Data，会自动加上很多的方法
import lombok.Data;

@Data
public class Book {
//    private Long id;
    private Long bookId; // 对应数据库中的 book_id
    private String title;
    private String author;
    private String description;
    private Integer stock;
    private Double price;

    // getter 和 setter 方法：自带了，使用lombok
}


//默认写入一些：
//        INSERT INTO books (title, author, price, stock, description)
//        VALUES ('The Catcher in the Rye', 'J.D. Salinger', 10.99, 100, 'A classic novel about adolescence.'),
//        ('To Kill a Mockingbird', 'Harper Lee', 12.50, 50, 'A novel set in the American South during the 1930s.'),
//        ('1984', 'George Orwell', 8.99, 200, 'A dystopian novel about totalitarianism.');

