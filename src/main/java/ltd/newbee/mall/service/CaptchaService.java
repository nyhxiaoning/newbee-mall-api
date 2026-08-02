package ltd.newbee.mall.service;

import ltd.newbee.mall.api.common.vo.CaptchaVO;

public interface CaptchaService {

    CaptchaVO generateCaptcha(String userName);

    boolean verifyCaptcha(String captchaKey, String captchaCode, String userName);
}