package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.query.GroupInfoQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.enums.ResponseCodeEnum;
import com.easychat.exception.BusinessException;
import com.easychat.services.GroupInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

/**
 * @ClassName AdminGroupController
 * @Description 管理员群组管理
 * @Author
 * @Date
 * */
@RestController("adminGroupInfoController")
@RequestMapping("/admin")
public class AdminGroupController extends ABaseController {
    @Resource
    private GroupInfoService groupInfoService;

    /**
     * 加载群组相关信息
     */
    @RequestMapping("/load_group")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO getGroup(GroupInfoQuery groupInfoQuery) {
        groupInfoQuery.setOrderBy("create_time desc");
        groupInfoQuery.setShowGroupOwnerNickName(true);
        groupInfoQuery.setShowMemberCount(true);
        PaginationResultVO resultVO = groupInfoService.findListByPage(groupInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 解散群组
     * */
    @RequestMapping("/dissolution_group")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO dissolutionGroup(@NotEmpty String groupId) throws BusinessException {
        // 判断该用户是否是从前端过来的
        GroupInfo groupInfo = groupInfoService.getByGroupId(groupId);
        if(groupInfo==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        groupInfoService.dissolutionGroup(groupInfo.getGroupOwnerId(), groupId);
        return getSuccessResponseVO(null);

    }
}
