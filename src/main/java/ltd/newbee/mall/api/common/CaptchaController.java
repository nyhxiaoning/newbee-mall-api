package ltd.newbee.mall.api.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.newbee.mall.api.common.vo.CaptchaVO;
import ltd.newbee.mall.service.CaptchaService;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(value = "v1", tags = "0.验证码接口")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @GetMapping({"/api/v1/captcha", "/manage-api/v1/captcha"})
    @ApiOperation(value = "获取验证码", notes = "返回base64图片和captchaKey；可选传入userName绑定验证码到指定用户")
    public Result<CaptchaVO> getCaptcha(@RequestParam(required = false) String userName) {
        CaptchaVO captchaVO = captchaService.generateCaptcha(userName);
        return ResultGenerator.genSuccessResult(captchaVO);
    }
}