package ltd.newbee.mall.controller;

import ltd.newbee.mall.entity.SaleGoods;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @RequestMapping("/test/conversion")
    public String ConversionTest(String goodsName, float weight, Boolean oneSale){
        System.out.println("goodsName"+ goodsName);
        System.out.println("weight"+weight);
        System.out.println("onSale"+oneSale);
        return "success";
    }
    // todo:管理拦截静态页面访问的默认位置：
    /**
     * http://localhost:28099/some-page
     * 访问的时候，可以默认打印：127.0.0.1/some-page
     */
    @RequestMapping("/some-page")
    public String somePage(){
        // 转发导静态页面
        return "http://127.0.0.1/some-page";
    }

    @RequestMapping("/home")
    public String Home(){
        // 转发导静态页面
        return "you address is wrong ，请访问：http://localhost:28099/swagger-ui/index.html";
    }

    @RequestMapping("/")
    public String blank(){
        // 转发导静态页面
        return "you address is wrong ，请访问：:http://localhost:28099/swagger-ui/index.html";
    }

    @RequestMapping(value = "/test/httpmessageconverter", method = RequestMethod.POST)
    public SaleGoods httpMessageConvertTest(@RequestBody SaleGoods saleGoods){
        System.out.println(saleGoods.toString());
        saleGoods.setType(saleGoods.getType());
        saleGoods.setGoodsName("商品名称"+saleGoods.getGoodsName());
        return saleGoods;
    }
}
