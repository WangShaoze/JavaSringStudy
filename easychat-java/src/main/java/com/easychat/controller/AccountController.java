package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.enums.ResponseCodeEnum;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisComponent;
import com.easychat.redis.RedisUtils;
import com.easychat.services.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @ClassName AccountController
 * @Description 账号控制层
 * @Author
 * @Date
 * */
@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/check_code")
    public ResponseVO checkCode(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey, code, Constants.REDIS_TIME_1MINUTE* 5L);
        String checkCodeBase64 = captcha.toBase64();

        Map<String, String> result = new HashMap<>();
        result.put("checkCode", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/register")
    public ResponseVO register(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty @Pattern(regexp = Constants.REGEXP_PASSWORD) String password,
                               @NotEmpty String nickName,
                               @NotEmpty String checkCode
    ){
        try {
            if (!checkCode.equals(redisUtils.get(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey))){
                throw new BusinessException("图片验证码不正确！");
            }
            userInfoService.register(email, nickName, password);
            return getSuccessResponseVO(null);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        } finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
    }


    @RequestMapping("/login")
    public ResponseVO login(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty String password,
                               @NotEmpty String checkCode
    ) throws BusinessException {
        try {
            if (!checkCode.equals(redisUtils.get(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey))){
                throw new BusinessException("图片验证码不正确！");
            }
            UserInfoVO userInfoVO = userInfoService.login(email, password);
            return getSuccessResponseVO(userInfoVO);
        } finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
    }

    @RequestMapping("/get_sys_setting")
    @GlobalInterceptor
    public ResponseVO getSysSetting(
    ){
        return getSuccessResponseVO(redisComponent.getSysSetting());
    }
}
