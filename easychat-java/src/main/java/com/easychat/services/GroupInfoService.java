package com.easychat.services;

import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.query.GroupInfoQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.enums.MessageTypeEnum;
import com.easychat.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description: 业务接口
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface GroupInfoService {

    /**
     * 解散群组操作
     *
     * @param groupOwnerId 群主id
     * @param groupId      群 id
     */
    void dissolutionGroup(String groupOwnerId, String groupId) throws BusinessException;


    /**
     * 保存或者更新群组信息
     */
    void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws BusinessException, IOException;

    /**
     * 添加或移除群成员
     */
    void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfoDto, String groupId, String selectContacts, Integer opType) throws BusinessException;


    /**
     * 退出或被剔出群聊
     * */
    void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum) throws BusinessException;

    /**
     * 根据条件查询列表
     */
    List<GroupInfo> findListByParam(GroupInfoQuery query);


    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(GroupInfoQuery query);


    /**
     * 分页查询
     */
    PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query);


    /**
     * 新增
     */
    Integer add(GroupInfo bean);


    /**
     * 批量新增
     */
    Integer addBatch(List<GroupInfo> listBean);


    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<GroupInfo> listBean);


    /**
     * 根据 GroupId查询
     */
    GroupInfo getByGroupId(String groupId);


    /**
     * 根据 GroupId更新
     */
    Integer updateByGroupId(GroupInfo bean, String groupId);


    /**
     * 根据 GroupId删除
     */
    Integer deleteByGroupId(String groupId);

}

