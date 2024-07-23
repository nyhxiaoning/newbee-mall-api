package ltd.newbee.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloController {
    @GetMapping("/test")
    @ResponseBody
    public String hello(){
        return "hello Spring Boot java java";
    }
}
