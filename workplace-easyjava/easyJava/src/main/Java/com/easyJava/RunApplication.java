package com.easyJava;

import com.easyJava.bean.Constants;
import com.easyJava.bean.TableInfo;
import com.easyJava.buidler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RunApplication {
    private static final Logger logger = LoggerFactory.getLogger(BuilderTable.class);

    public static void main(String[] args) {
        logger.info("基础项目构建中 ... ");
        List<TableInfo> tableInfoList = BuilderTable.getTables();
//        logger.error(" easychat 数据库的表信息: {}", JsonUtils.convertObj2Json(tableInfoList));

        for (TableInfo tableInfo :
                tableInfoList) {
            // 创建PO
            BuilderPO.execute(tableInfo);
            BuilderQuery.execute(tableInfo);
            BuilderMapper.execute(tableInfo);
            BuilderMapperXML.execute(tableInfo);
            BuilderService.execute(tableInfo);
            BuildServiceImpl.execute(tableInfo);
            BuildController.execute(tableInfo);
        }
        BuilderBase.execute();
        BuilderRunApplication.execute();


        logger.info("基础框架生成成功,项目文件地址:{}", Constants.PATH_PROJECT);
    }
}
