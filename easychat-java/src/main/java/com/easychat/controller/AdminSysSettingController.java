package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.redis.RedisComponent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName AdminSysSettingController
 * @Description 管理员系统设置
 * @Author
 * @Date
 * */
@RestController("adminSysSettingController")
@RequestMapping("admin")
public class AdminSysSettingController extends ABaseController {

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    /**
     * 获取系统设置
     * */
    @RequestMapping("/get_sys_setting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO getSysSetting(){
        return getSuccessResponseVO(redisComponent.getSysSetting());
    }

    /**
     * 保存系统设置
     *
     * @param sysSettingDto 系统设置对象
     * @param robotFile 机器人头像
     * @param robotCover 机器人头像缩略图
     * */
    @RequestMapping("/save_sys_setting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO saveSysSetting(
                SysSettingDto sysSettingDto,
                MultipartFile robotFile,
                MultipartFile robotCover) throws IOException {
        // 保存头像
        if (robotFile!=null){
            String baseFolder = appConfig.getProjectFolder()+ Constants.FILE_FOLDER_FILE;
            File targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_FILE_AVATAR_NAME);
            if (!targetFileFolder.exists()){
                targetFileFolder.mkdirs();
            }
            String filePath = targetFileFolder.getPath()+"/"+Constants.ROBOT_UID+Constants.IMAGE_SUFFIX;
            String filePathCover = targetFileFolder.getPath()+"/"+Constants.ROBOT_UID+Constants.COVER_IMAGE_SUFFIX;
            robotFile.transferTo(new File(filePath));
            robotCover.transferTo(new File(filePathCover));
        }

        // 保存系统设置
        redisComponent.saveSysSetting(sysSettingDto);
        return getSuccessResponseVO(redisComponent.getSysSetting());
    }
}
