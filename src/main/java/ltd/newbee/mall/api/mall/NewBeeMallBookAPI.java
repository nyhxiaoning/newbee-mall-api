package ltd.newbee.mall.api.mall;

import ltd.newbee.mall.api.admin.NewBeeAdminCarouselAPI;
import ltd.newbee.mall.service.NewBeeMallBookService;
import org.springframework.web.bind.annotation.*;

import ltd.newbee.mall.entity.Book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 引入Result类，统一处理当前的返回值
 */
import ltd.newbee.mall.util.*;
@RestController
@RequestMapping("/api/v1")
public class NewBeeMallBookAPI {
    private static final Logger logger = LoggerFactory.getLogger(NewBeeAdminCarouselAPI.class);


    private final NewBeeMallBookService bookService;

    public NewBeeMallBookAPI(NewBeeMallBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public Result list() {
        logger.info("访问mall前台接口实现位置：Get接口");
        return ResultGenerator.genSuccessResult(bookService.getBooks());
    }

    @PostMapping("/book")
    public Result createBook(@RequestBody Book book) {
        // 假设 Book 是一个 DTO 类，包含图书信息
        bookService.createBook(book);
        return ResultGenerator.genSuccessResult("图书创建成功");
    }

    @DeleteMapping("/book/{id}")
    public Result deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResultGenerator.genSuccessResult("图书删除成功");
    }

    // 其他面向用户的书籍 API
}
