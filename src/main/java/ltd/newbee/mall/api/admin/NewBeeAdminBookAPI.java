package ltd.newbee.mall.api.admin;

import ltd.newbee.mall.entity.Book;
import ltd.newbee.mall.service.NewBeeMallBookService;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/manage-api/v1")
public class NewBeeAdminBookAPI {

    private static final Logger logger = LoggerFactory.getLogger(NewBeeAdminBookAPI.class);

    @Resource
    private NewBeeMallBookService bookService;

    /**
     * 获取图书列表
     */
    @GetMapping("/list")
    public Result list() {
        logger.info("访问admin后台接口：获取图书列表");
        return ResultGenerator.genSuccessResult(bookService.getBooks());
    }

    /**
     * 创建新图书
     */
    @PostMapping("/create")
    public Result createBook(@RequestBody @Valid Book book) {
        // TODO:这里默认生成了当前的api的请求体结构内容：
        logger.info("访问admin后台接口：创建图书");
        String result = bookService.createBook(book);
        if (result.equals("SUCCESS")) {
            return ResultGenerator.genSuccessResult("图书创建成功");
        }
        return ResultGenerator.genFailResult(result);
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteBook(@PathVariable Long id) {
        logger.info("访问admin后台接口：删除图书");
        String result = bookService.deleteBook(id);
        if (result.equals("SUCCESS")) {
            return ResultGenerator.genSuccessResult("图书删除成功");
        }
        return ResultGenerator.genFailResult(result);
    }

    /**
     * 更新图书信息
     */
    @PutMapping("/update")
    public Result updateBook(@RequestBody @Valid Book book) {
        logger.info("访问admin后台接口：更新图书信息");
        String result = bookService.updateBook(book);
        if (result.equals("SUCCESS")) {
            return ResultGenerator.genSuccessResult("图书更新成功");
        }
        return ResultGenerator.genFailResult(result);
    }

    /**
     * 获取单个图书信息
     */
    @GetMapping("/detail/{id}")
    public Result getBookDetail(@PathVariable Long id) {
        logger.info("访问admin后台接口：获取图书详情");
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResultGenerator.genSuccessResult(book);
        }
        return ResultGenerator.genFailResult("图书不存在");
    }
}
