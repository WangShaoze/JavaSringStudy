package com.runyin.exceptions;

import com.runyin.enums.ResponseCodeEnum;

public class BusinessException extends Exception{
    private ResponseCodeEnum responseCodeEnum;
    private Integer code;
    private String message;

    public BusinessException(String message, Throwable e){
        super(message, e);
        this.message =message;
    }

    public BusinessException(String message){
        super(message);
        this.message =message;
    }

    public BusinessException(Throwable e){super(e);}

    public BusinessException(ResponseCodeEnum responseCodeEnum){
        super(responseCodeEnum.getMsg());
        this.responseCodeEnum = responseCodeEnum;
        this.code = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getMsg();
    }

    public BusinessException(Integer code, String message){
        super(message);
        this.code=code;
        this.message=message;
    }

    public ResponseCodeEnum getResponseCodeEnum() {
        return responseCodeEnum;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 重写 fillInStackTrace 业务异常不需要提供堆栈信息，提高效率
     * */

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
