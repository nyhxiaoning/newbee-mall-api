package ltd.newbee.mall.service.impl;

import ltd.newbee.mall.api.admin.NewBeeAdminCarouselAPI;
import ltd.newbee.mall.dao.BookMapper;
import ltd.newbee.mall.entity.Book;
import ltd.newbee.mall.service.NewBeeMallBookService;
import ltd.newbee.mall.common.ServiceResultEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NewBeeMallBookServiceImpl implements NewBeeMallBookService {

    private static final Logger logger = LoggerFactory.getLogger(NewBeeAdminCarouselAPI.class);

    @Resource
    private BookMapper bookMapper;

    @Override
    public List<Book> getBooks() {
        return bookMapper.selectAllBooks();
    }

    @Override
    public String createBook(Book book) {
        logger.info("创建图书class");
        if (bookMapper.insertBook(book) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }

        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String deleteBook(Long bookId) {
        logger.info("delete图书class");
        if (bookMapper.deleteBookById(bookId) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateBook(Book book) {
        if (bookMapper.updateBookById(book) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookMapper.selectBookById(bookId);
    }
}
