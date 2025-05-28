package ltd.newbee.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("ltd.newbee.mall.dao")
@SpringBootApplication
public class NewBeeMallAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewBeeMallAPIApplication.class, args);
        System.out.println("28099端口服务开启，注意mysql密码是不是正确");
    }

}
