package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.enums.PageSize;
import com.easychat.exception.BusinessException;
import com.easychat.services.UserInfoService;
import com.easychat.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AdminUserInfoController
 * @Description 管理员用户管理
 * @Author
 * @Date
 * */
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
        if (userInfoQuery.getPageNo()==null){
            userInfoQuery.setPageNo(Constants.ONE);
            userInfoQuery.setPageSize(PageSize.SIZE10.getSize());
        }
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
