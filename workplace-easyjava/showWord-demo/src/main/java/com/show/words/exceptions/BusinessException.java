package com.show.words.exceptions;

import com.show.words.enums.ResponseCodeEnum;

public class BusinessException extends Exception{
    private ResponseCodeEnum responseCodeEnum;
    private Integer code;
    private String msg;


    public BusinessException(String message, Throwable e){
        super(message,e);
        this.msg = message;
    }
    public BusinessException(String message){
        super(message);
        this.msg = message;
    }
    public BusinessException(Throwable e){
        super(e);
    }
    public BusinessException(ResponseCodeEnum responseCodeEnum){
        super(responseCodeEnum.getMsg());
        this.responseCodeEnum = responseCodeEnum;
        this.code = responseCodeEnum.getCode();
        this.msg = responseCodeEnum.getMsg();
    }
    public BusinessException(Integer code, String message){
        super(message);
        this.code = code;
        this.msg =message;
    }

    public ResponseCodeEnum getResponseCodeEnum() {
        return responseCodeEnum;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 重写 fillInStackTrace 业务异常不需要提供堆栈信息，提高效率
     * */

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
