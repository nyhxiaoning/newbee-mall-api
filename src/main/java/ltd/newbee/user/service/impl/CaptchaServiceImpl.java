package ltd.newbee.user.service.impl;

import com.wf.captcha.SpecCaptcha;
import ltd.newbee.common.CaptchaVO;
import ltd.newbee.common.Constants;
import ltd.newbee.user.service.CaptchaService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public CaptchaVO generateCaptcha(String userName) {
        SpecCaptcha captcha = new SpecCaptcha(130, 48, Constants.CAPTCHA_LENGTH);
        String code = captcha.text().toLowerCase();
        String base64Image = captcha.toBase64();

        String key = UUID.randomUUID().toString().replace("-", "");
        String redisKey = Constants.CAPTCHA_REDIS_KEY_PREFIX + key;

        // 存储格式：有userName则绑定 code|userName，无则只存 code
        String value = StringUtils.hasText(userName) ? code + "|" + userName : code;
        stringRedisTemplate.opsForValue().set(redisKey, value,
                Constants.CAPTCHA_EXPIRE_SECONDS, TimeUnit.SECONDS);

        return new CaptchaVO(key, base64Image);
    }

    @Override
    public boolean verifyCaptcha(String captchaKey, String captchaCode, String userName) {
        if (captchaKey == null || captchaCode == null
                || captchaKey.trim().isEmpty() || captchaCode.trim().isEmpty()) {
            return false;
        }
        String redisKey = Constants.CAPTCHA_REDIS_KEY_PREFIX + captchaKey;
        String storedValue = stringRedisTemplate.opsForValue().get(redisKey);
        // 立即删除，一次性使用
        stringRedisTemplate.delete(redisKey);

        if (storedValue == null) {
            return false;
        }

        // 解析存储值：code|userName 或 plain code
        int separatorIndex = storedValue.indexOf('|');
        String storedCode;
        String storedUser = null;
        if (separatorIndex > 0) {
            storedCode = storedValue.substring(0, separatorIndex);
            storedUser = storedValue.substring(separatorIndex + 1);
        } else {
            storedCode = storedValue;
        }

        // 验证码不匹配
        if (!storedCode.equalsIgnoreCase(captchaCode.trim())) {
            return false;
        }

        // 如果验证码绑定了userName，校验账号是否匹配
        if (storedUser != null) {
            return userName != null && storedUser.equals(userName);
        }

        return true;
    }
}