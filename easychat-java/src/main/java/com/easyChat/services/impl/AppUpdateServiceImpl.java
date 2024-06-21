package com.easyChat.services.impl;

import com.easyChat.entity.config.AppConfig;
import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.po.AppUpdate;
import com.easyChat.entity.query.AppUpdateQuery;
import com.easyChat.enums.AppUpdateFileTypeEnum;
import com.easyChat.enums.AppUpdateStatusEnum;
import com.easyChat.enums.ResponseCodeEnum;
import com.easyChat.exception.BusinessException;
import com.easyChat.services.AppUpdateService;
import com.easyChat.mappers.AppUpdateMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.enums.DateTimePatternEnum;
import com.easyChat.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.easyChat.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description: app发布 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/21
 */
@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {
    @Resource
    private AppUpdateMapper<AppUpdate, AppUpdateQuery> appUpdateMapper;
    @Resource
    private AppConfig appConfig;

    /**
     * 保存更新
     * 版本修改或者新增需要遵循的原则:
     * 1.新增时，版本不小于最新版
     * 2.不能从一个版本改到另外一个数据库中已有的版本
     *
     * @param appUpdate 需要更新的对象
     * @param file      app更新包
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws BusinessException, IOException {
        AppUpdateFileTypeEnum appUpdateFileTypeEnum = AppUpdateFileTypeEnum.getByType(appUpdate.getFileType());
        if (null == appUpdateFileTypeEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if(appUpdate.getId()!=null){
            AppUpdate appUpdateInDB = this.getById(appUpdate.getId());
            if (!appUpdateInDB.getStatus().equals(AppUpdateStatusEnum.INIT.getStatus())){
                throw new BusinessException("该状态下不能修改！！");
            }
        }
        AppUpdateQuery updateQuery = new AppUpdateQuery();
        updateQuery.setOrderBy("id desc");
        updateQuery.setSimplePage(new SimplePage(0, 1));  // 取出第一条
        List<AppUpdate> appUpdateList = appUpdateMapper.selectList(updateQuery);
        if (!appUpdateList.isEmpty()) {
            // 对版本进行校验
            AppUpdate latest = appUpdateList.get(0);  // 数据库中的最新版
            Long dbLatestVersion = Long.parseLong(latest.getVersion().replace(".", ""));
            Long currentVersion = Long.parseLong(appUpdate.getVersion().replace(",", ""));

            // 新增时版本必须大于数据库中的最新版
            if (appUpdate.getId() == null && currentVersion <= dbLatestVersion) {
                throw new BusinessException("当前版本必须大于历史版本！");
            }

            // 用户不能将已有版本改为数据库中已经存在的其他版本
            AppUpdate versionDB = appUpdateMapper.selectByVersion(appUpdate.getVersion());
            if (versionDB != null && appUpdate.getId() != null && !versionDB.getId().equals(appUpdate.getId())) {
                throw new BusinessException("不能修改为其他版本，只能在当前版本上修改！");
            }
        }

        if (appUpdate.getId() == null) {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateStatusEnum.INIT.getStatus());  // 初始值设置为未发布
            appUpdateMapper.insert(appUpdate);
        } else {
            appUpdate.setStatus(null);
            appUpdate.setGrayscaleUid(null);
            appUpdateMapper.updateById(appUpdate, appUpdate.getId());
        }

        if (file == null) {
            // 保存 app更新包
            File folder = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file.transferTo(new File(folder.getAbsolutePath() + "/" + appUpdate.getId() + Constants.APP_EXE_SUFFIX));
        }
    }

    /**
     * 发布更新
     * @param id 需要发布版本的id
     * @param status 当前的状态
     * @param greyscaleUid  需要灰度发布时，必须提供灰度发布的人员UID
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postUpdate(Integer id, Integer status, String greyscaleUid) throws BusinessException {
        AppUpdateStatusEnum appUpdateStatusEnum = AppUpdateStatusEnum.getByStatus(status);
        if (null == appUpdateStatusEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (appUpdateStatusEnum.equals(AppUpdateStatusEnum.GREYSCALE) && StringUtils.isEmpty(greyscaleUid)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if(!appUpdateStatusEnum.equals(AppUpdateStatusEnum.GREYSCALE)){
            greyscaleUid = "";
        }

        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setStatus(status);
        appUpdate.setGrayscaleUid(greyscaleUid);
        appUpdateMapper.updateById(appUpdate, id);
    }

    @Override
    public AppUpdate getLatestUpdate(String appVersion, String uid) {
        return appUpdateMapper.selectLatestUpdate(appVersion,uid);
    }


    /**
     * 根据条件查询列表
     */
    public List<AppUpdate> findListByParam(AppUpdateQuery query) {

        return this.appUpdateMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(AppUpdateQuery query) {

        return this.appUpdateMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery query) {
        Integer count = this.findCountByParam(query);
        SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
        query.setSimplePage(simplePage);
        List<AppUpdate> list = this.findListByParam(query);
        PaginationResultVO<AppUpdate> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(AppUpdate bean) {
        return this.appUpdateMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改
     */
    public Integer addOrUpdateBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据 Id查询
     */
    public AppUpdate getById(Integer id) {
        return this.appUpdateMapper.selectById(id);
    }

    /**
     * 根据 Id更新
     */
    public Integer updateById(AppUpdate bean, Integer id) {

        return this.appUpdateMapper.updateById(bean, id);
    }

    /**
     * 根据 Id删除
     */
    public Integer deleteById(Integer id) throws BusinessException {
        AppUpdate appUpdateInDB = this.getById(id);
        if (!appUpdateInDB.getStatus().equals(AppUpdateStatusEnum.INIT.getStatus())){
            throw new BusinessException("该状态下不能删除！！");
        }
        return this.appUpdateMapper.deleteById(id);
    }

    /**
     * 根据 Version查询
     */
    public AppUpdate getByVersion(String version) {
        return this.appUpdateMapper.selectByVersion(version);
    }

    /**
     * 根据 Version更新
     */
    public Integer updateByVersion(AppUpdate bean, String version) {

        return this.appUpdateMapper.updateByVersion(bean, version);
    }

    /**
     * 根据 Version删除
     */
    public Integer deleteByVersion(String version) {

        return this.appUpdateMapper.deleteByVersion(version);
    }
}

