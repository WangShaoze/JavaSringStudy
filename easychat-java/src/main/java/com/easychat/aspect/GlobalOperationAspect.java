package com.easychat.aspect;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.enums.ResponseCodeEnum;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisUtils;
import jodd.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * GlobalOperationAspect
 * 全局切面
 * */
@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
    @Resource
    private RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(GlobalOperationAspect.class);


    @Before("@annotation(com.easychat.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point) throws BusinessException {
        try {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            GlobalInterceptor globalInterceptor = method.getAnnotation(GlobalInterceptor.class);
            if (globalInterceptor==null){
                return;
            }
            if (globalInterceptor.checkAdmin()||globalInterceptor.checkLogin()){
                checkLogin(globalInterceptor.checkAdmin());
            }
        }catch (BusinessException e){
            logger.error("全局拦截异常",e);
            throw e;
        }catch (Exception e){
            logger.error("全局拦截异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }catch (Throwable e){
            logger.error("全局拦截异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    public void checkLogin(Boolean checkAdmin) throws BusinessException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        if (StringUtil.isEmpty(token)){  // 检查登录状态
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN+token);
        if (tokenUserInfoDto==null){  // 检查登录状态
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if (checkAdmin&&!tokenUserInfoDto.getAdmin()){  // 检查发现没有权限
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
    }

}
