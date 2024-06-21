package com.easyChat.controller;

import com.easyChat.annotation.GlobalInterceptor;
import com.easyChat.entity.config.AppConfig;
import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.po.AppUpdate;
import com.easyChat.entity.query.AppUpdateQuery;
import com.easyChat.entity.vo.AppUpdateVO;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.enums.AppUpdateFileTypeEnum;
import com.easyChat.enums.ResponseCodeEnum;
import com.easyChat.exception.BusinessException;
import com.easyChat.services.AppUpdateService;
import com.easyChat.utils.CopyTools;
import com.easyChat.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;

/**
 * @Description: app发布 Controller
 * @author: 王绍泽
 * @date: 2024/06/21
 */
@RestController("updateController")
@RequestMapping("/update")
public class UpdateController extends ABaseController{

    @Resource
    private AppUpdateService appUpdateService;

    @Resource
    private AppConfig appConfig;

    /**
     * 获取更新列表
     * @param appVersion   当前app的版本
     *                     这里的参数不可以使用 @NotEmpty  这个接口发起时，app处于启动阶段，
     *                     且是自动发起的，如果没有更新需要，有使用了 @NotEmpty 修饰该参数，
     *                     可能会出现错误，并难以排查
     * @param uid  当前用户id
     *             这里的参数不可以使用 @NotEmpty  这个接口发起时，app处于启动阶段，
     *             且是自动发起的，如果没有更新需要，有使用了 @NotEmpty 修饰该参数，
     *             可能会出现错误，并难以排查
     * */
    @RequestMapping("/check_version")
    @GlobalInterceptor
    public ResponseVO checkVersion(String appVersion, String uid) throws BusinessException {
        if (StringUtils.isEmpty(appVersion) || StringUtils.isEmpty(uid)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        AppUpdate appUpdate = appUpdateService.getLatestUpdate(appVersion, uid);
        if (appUpdate==null){
            return getSuccessResponseVO(null);
        }

        AppUpdateVO appUpdateVO = CopyTools.copy(appUpdate, AppUpdateVO.class);

        if (appUpdate.getFileType().equals(AppUpdateFileTypeEnum.LOCAL.getType())){
            File file = new File(appConfig.getProjectFolder()+ Constants.APP_UPDATE_FOLDER+appUpdate.getId()+Constants.APP_EXE_SUFFIX);
            appUpdateVO.setSize(file.length());
        }else{
            appUpdateVO.setSize(0L);
        }
        appUpdateVO.setUpdateList(Arrays.asList(appUpdate.getUpdateDescArray()));  // 数组转化为列表
        String fileName = Constants.APP_NAME+appUpdate.getVersion()+Constants.APP_EXE_SUFFIX;
        appUpdateVO.setFileName(fileName);
        return getSuccessResponseVO(appUpdateVO);
    }

}
