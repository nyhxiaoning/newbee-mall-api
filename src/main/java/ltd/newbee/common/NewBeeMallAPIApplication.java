package ltd.newbee.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan({
    "ltd.newbee.goods.dao",
    "ltd.newbee.order.dao",
    "ltd.newbee.user.dao"
})
@SpringBootApplication
@ComponentScan("ltd.newbee")
public class NewBeeMallAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewBeeMallAPIApplication.class, args);
        System.out.println("28099 端口服务开启，注意mysql密码是不是正确");
    }

}
