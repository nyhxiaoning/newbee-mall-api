package ltd.newbee.user.service;

import ltd.newbee.common.CaptchaVO;

public interface CaptchaService {

    CaptchaVO generateCaptcha(String userName);

    boolean verifyCaptcha(String captchaKey, String captchaCode, String userName);
}