package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.query.UserInfoBeautyQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.enums.PageSize;
import com.easychat.exception.BusinessException;
import com.easychat.services.UserInfoBeautyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AdminUserInfoBeautyController
 * @Description 管理员靓号管理
 * @Author
 * @Date
 * */
@RestController("adminUserInfoBeautyController")
@RequestMapping("/admin")
public class AdminUserInfoBeautyController extends ABaseController {
    @Resource
    private UserInfoBeautyService userInfoBeautyService;


    /**
     * 获取数据库中的靓号列表
     */
    @RequestMapping("load_beauty_account_list")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO getUserInfo(UserInfoBeautyQuery userInfoBeautyQuery) {
        userInfoBeautyQuery.setOrderBy("id desc");
        if (userInfoBeautyQuery.getPageNo()==null){
            userInfoBeautyQuery.setPageNo(Constants.ONE);
            userInfoBeautyQuery.setPageSize(PageSize.SIZE10.getSize());
        }
        PaginationResultVO resultVO = userInfoBeautyService.findListByPage(userInfoBeautyQuery);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 保存靓号
     */
    @RequestMapping("save_beauty_account")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO saveBeautyAccount(UserInfoBeauty userInfoBeauty) throws BusinessException {
        userInfoBeautyService.saveAccount(userInfoBeauty);
        return getSuccessResponseVO(null);
    }

    /**
     * 删除靓号
     *
     * @param id 需要删除的靓号id
     */
    @RequestMapping("del_beauty_account")
    @GlobalInterceptor(checkAdmin = true)
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO delBeautyAccount(@NotNull Integer id) {
        userInfoBeautyService.deleteById(id);
        return getSuccessResponseVO(null);
    }

}
