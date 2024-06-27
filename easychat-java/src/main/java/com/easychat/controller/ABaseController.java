package com.easychat.controller;

import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.enums.ResponseCodeEnum;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class ABaseController {
    protected static final  String STATUS_SUCCESS = "success";
    protected static final  String STATUS_ERROR = "error";

    @Resource
    private RedisUtils redisUtils;


    protected <T> ResponseVO getSuccessResponseVO(T t){
        ResponseVO<T> responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }
    protected <T> ResponseVO getSuccessResponseVO(){
        ResponseVO<T> responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(null);
        return responseVO;
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_ERROR);
        if (e.getCode()==null){
            responseVO.setCode(ResponseCodeEnum.CODE_600.getCode());
        }else {
            responseVO.setCode(e.getCode());
        }
        responseVO.setInfo(e.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t){
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUS_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }

    protected TokenUserInfoDto getTokenUserInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN+token);
        return tokenUserInfoDto;
    }
}
