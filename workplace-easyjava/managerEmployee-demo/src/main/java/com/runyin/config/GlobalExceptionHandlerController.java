package com.runyin.config;

import com.runyin.enums.ResponseCodeEnum;
import com.runyin.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;


@RestControllerAdvice
public class GlobalExceptionHandlerController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler
    Object handlerException(Exception e, HttpServletRequest request){
        logger.error("请求出错，错误地址:{},错误信息:{}", request.getRequestURI(),e);

        ResponseVO ajaxResponse = new ResponseVO();

        if (e instanceof NoHandlerFoundException){
            // 404
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }else if (e instanceof BusinessException){
            // 业务异常
            BusinessException businessException = (BusinessException) e;
            ajaxResponse.setCode(businessException.getCode());
            ajaxResponse.setInfo(businessException.getMessage());
            ajaxResponse.setStatus(STATUS_ERROR);
        }else if (e instanceof BindException){
            // 参数异常
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_600.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);

        }else if (e instanceof DuplicateKeyException){
            // 信息已经存在
            ajaxResponse.setCode(ResponseCodeEnum.CODE_601.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_601.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }else{
            // 其他问题
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        return ajaxResponse;
    }
}
