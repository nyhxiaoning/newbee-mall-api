package ltd.newbee.common;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class CaptchaVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("验证码key，登录时需传回")
    private String captchaKey;

    @ApiModelProperty("验证码图片(base64格式，可直接作为img src)")
    private String captchaImage;

    public CaptchaVO(String captchaKey, String captchaImage) {
        this.captchaKey = captchaKey;
        this.captchaImage = captchaImage;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String getCaptchaImage() {
        return captchaImage;
    }

    public void setCaptchaImage(String captchaImage) {
        this.captchaImage = captchaImage;
    }
}