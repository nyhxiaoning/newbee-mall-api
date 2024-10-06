package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BookMapper {
    // 查询所有图书
    List<Book> selectAllBooks();

    // 插入图书记录
    int insertBook(Book book);

    // 根据ID删除图书记录
    int deleteBookById(Long bookId);

    // 根据ID更新图书信息
    int updateBookById(Book book);

    // 根据ID查询单个图书
    Book selectBookById(Long bookId);
}
