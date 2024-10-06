package ltd.newbee.mall.service;

import ltd.newbee.mall.entity.Book;
import java.util.List;

public interface NewBeeMallBookService {
    // 获取所有图书
    List<Book> getBooks();

    // 创建新图书
    String createBook(Book book);

    // 删除图书
    String deleteBook(Long bookId);

    // 更新图书信息
    String updateBook(Book book);

    // 根据ID获取单个图书信息
    Book getBookById(Long bookId);
}
