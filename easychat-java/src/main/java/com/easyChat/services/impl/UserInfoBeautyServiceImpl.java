package com.easyChat.services.impl;

import com.easyChat.entity.po.UserInfo;
import com.easyChat.entity.po.UserInfoBeauty;
import com.easyChat.entity.query.UserInfoBeautyQuery;
import com.easyChat.entity.query.UserInfoQuery;
import com.easyChat.enums.BeautyAccountStatusEnum;
import com.easyChat.enums.ResponseCodeEnum;
import com.easyChat.exception.BusinessException;
import com.easyChat.mappers.UserInfoMapper;
import com.easyChat.services.UserInfoBeautyService;
import com.easyChat.mappers.UserInfoBeautyMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 靓号表 业务接口实现
 * @author: 王绍泽
 * @date: 2024/05/27
 */
@Service("userInfoBeautyService")
public class UserInfoBeautyServiceImpl implements UserInfoBeautyService {
    @Resource
    private UserInfoBeautyMapper<UserInfoBeauty, UserInfoBeautyQuery> userInfoBeautyMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /**
     * 保存靓号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAccount(UserInfoBeauty userInfoBeauty) throws BusinessException {
        if (userInfoBeauty.getId() != null) {
            UserInfoBeauty dbBeauty = userInfoBeautyMapper.selectById(userInfoBeauty.getId());
            if (dbBeauty.getStatus().equals(BeautyAccountStatusEnum.USED.getStatus())) {  // 已经使用过的账号不可以再用
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        UserInfoBeauty dbBeauty = userInfoBeautyMapper.selectByEmail(userInfoBeauty.getEmail());

        // 新增的时候判断靓号邮箱是否已经存在
        if (userInfoBeauty.getId() == null && dbBeauty != null) {
            throw new BusinessException("靓号邮箱已经存在！！");
        }

        // 修改时判断邮箱是否存在
        if (userInfoBeauty.getId() != null && dbBeauty != null && dbBeauty.getId() != null && !dbBeauty.getId().equals(userInfoBeauty.getId())) {
            throw new BusinessException("邮箱已经存在，你不能修改其他人的靓号信息！");
        }


        // 判断靓号是否存在
        dbBeauty = userInfoBeautyMapper.selectByUserId(userInfoBeauty.getUserId());
        if (userInfoBeauty.getId() == null && dbBeauty != null){
            throw new BusinessException("靓号已经存在！！");
        }
        if (userInfoBeauty.getId() != null && dbBeauty != null && dbBeauty.getId() != null && !dbBeauty.getId().equals(userInfoBeauty.getId())) {
            throw new BusinessException("该靓号已经存在，你不能修改其他人的靓号信息！");
        }

        // 判断邮箱是否注册
        UserInfo userInfo = userInfoMapper.selectByEmail(userInfoBeauty.getEmail());
        if (userInfo !=null){
            throw new BusinessException("靓号邮箱已经被注册！！");
        }

        userInfo = userInfoMapper.selectByUserId(userInfoBeauty.getUserId());
        if (userInfo !=null){
            throw new BusinessException("靓号已经被注册！！");
        }

        if (userInfoBeauty.getId()!=null) {
            this.userInfoBeautyMapper.updateById(userInfoBeauty, userInfoBeauty.getId());
        }else{
            userInfoBeauty.setStatus(BeautyAccountStatusEnum.NO_USE.getStatus());
            this.userInfoBeautyMapper.insert(userInfoBeauty);
        }
    }

    /**
     * 根据条件查询列表
     */
    public List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query) {

        return this.userInfoBeautyMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserInfoBeautyQuery query) {

        return this.userInfoBeautyMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery query) {
        Integer count = this.findCountByParam(query);
        SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
        query.setSimplePage(simplePage);
        List<UserInfoBeauty> list = this.findListByParam(query);
        PaginationResultVO<UserInfoBeauty> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserInfoBeauty bean) {
        return this.userInfoBeautyMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserInfoBeauty> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoBeautyMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改
     */
    public Integer addOrUpdateBatch(List<UserInfoBeauty> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoBeautyMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据 Id查询
     */
    public UserInfoBeauty getById(Integer id) {
        return this.userInfoBeautyMapper.selectById(id);
    }

    /**
     * 根据 Id更新
     */
    public Integer updateById(UserInfoBeauty bean, Integer id) {

        return this.userInfoBeautyMapper.updateById(bean, id);
    }

    /**
     * 根据 Id删除
     */
    public Integer deleteById(Integer id) {

        return this.userInfoBeautyMapper.deleteById(id);
    }

    /**
     * 根据 UserId查询
     */
    public UserInfoBeauty getByUserId(String userId) {
        return this.userInfoBeautyMapper.selectByUserId(userId);
    }

    /**
     * 根据 UserId更新
     */
    public Integer updateByUserId(UserInfoBeauty bean, String userId) {

        return this.userInfoBeautyMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据 UserId删除
     */
    public Integer deleteByUserId(String userId) {

        return this.userInfoBeautyMapper.deleteByUserId(userId);
    }

    /**
     * 根据 Email查询
     */
    public UserInfoBeauty getByEmail(String email) {
        return this.userInfoBeautyMapper.selectByEmail(email);
    }

    /**
     * 根据 Email更新
     */
    public Integer updateByEmail(UserInfoBeauty bean, String email) {

        return this.userInfoBeautyMapper.updateByEmail(bean, email);
    }

    /**
     * 根据 Email删除
     */
    public Integer deleteByEmail(String email) {

        return this.userInfoBeautyMapper.deleteByEmail(email);
    }
}

