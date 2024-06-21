package com.easyChat.services;

import com.easyChat.entity.po.AppUpdate;
import com.easyChat.entity.query.AppUpdateQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description: app发布 业务接口
 * @author: 王绍泽
 * @date: 2024/06/21
 */
public interface AppUpdateService {


    /**
     * 保存更新
     * @param appUpdate  需要更新的对象
     * @param file app更新包
     */
    void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws BusinessException, IOException;


    /**
     * 发布更新
     * @param id 需要发布版本的id
     * @param status 当前的状态
     * @param greyscaleUid  需要灰度发布时，必须提供灰度发布的人员UID
     * */
    void postUpdate(Integer id, Integer status, String greyscaleUid) throws BusinessException;


    AppUpdate getLatestUpdate(String appVersion, String uid);


    /**
     * 根据条件查询列表
     */
    List<AppUpdate> findListByParam(AppUpdateQuery query);


    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(AppUpdateQuery query);


    /**
     * 分页查询
     */
    PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery query);


    /**
     * 新增
     */
    Integer add(AppUpdate bean);


    /**
     * 批量新增
     */
    Integer addBatch(List<AppUpdate> listBean);


    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<AppUpdate> listBean);


    /**
     * 根据 Id查询
     */
    AppUpdate getById(Integer id);


    /**
     * 根据 Id更新
     */
    Integer updateById(AppUpdate bean, Integer id);


    /**
     * 根据 Id删除
     */
    Integer deleteById(Integer id) throws BusinessException;


    /**
     * 根据 Version查询
     */
    AppUpdate getByVersion(String version);


    /**
     * 根据 Version更新
     */
    Integer updateByVersion(AppUpdate bean, String version);


    /**
     * 根据 Version删除
     */
    Integer deleteByVersion(String version);
}

