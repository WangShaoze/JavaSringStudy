package com.easyChat.controller;

import com.easyChat.annotation.GlobalInterceptor;
import com.easyChat.entity.query.UserInfoQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.exception.BusinessException;
import com.easyChat.services.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController("adminUserInfoController")
@RequestMapping("/admin")
public class AdminUserInfoController extends ABaseController {
    @Resource
    private UserInfoService userInfoService;


    /**
     * 获取数据库中的用户列表
     */
    @RequestMapping("load_user")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO getUserInfo(UserInfoQuery userInfoQuery) {
        userInfoQuery.setOrderBy("create_time desc");
        PaginationResultVO resultVO = userInfoService.findListByPage(userInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 更新用户状态
     *
     * @param status 用户当前状态
     * @param userId 用户id
     */
    @RequestMapping("update_user_status")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO updateUserStatus(@NotNull Integer status, @NotEmpty String userId) throws BusinessException {
        userInfoService.updateUserStatus(status, userId);
        return getSuccessResponseVO(null);
    }


    /**
     * 强制下线
     *
     * @param userId  用户id
     */
    @RequestMapping("force_offline")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO forceOffline(@NotEmpty String userId) throws BusinessException {
        userInfoService.forceOffline(userId);
        return getSuccessResponseVO(null);
    }

}
