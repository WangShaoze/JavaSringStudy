package com.easychat.entity.config;

import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {
    /**
     * websocket 端口
     * */
    @Value("${ws.port:}")
    private Integer wsPort;

    /**
     * 文件目录
     * */
    @Value("${project.folder:}")
    private String projectFolder;

    /**
     * 超级管理员邮箱
     * */
    @Value("${admin.emails:}")
    private String adminEmails;

    public Integer getWsPort() {
        return wsPort;
    }

    public void setWsPort(Integer wsPort) {
        this.wsPort = wsPort;
    }

    public String getProjectFolder() {
        if (StringUtil.isEmpty(projectFolder)||!projectFolder.endsWith("/")){
            projectFolder += "/";
        }
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public String getAdminEmails() {
        return adminEmails;
    }

    public void setAdminEmails(String adminEmails) {
        this.adminEmails = adminEmails;
    }
}
