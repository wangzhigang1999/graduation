package com.bupt.graduation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 创建文件夹的工具类
 *
 * @author wangz
 */
public class MakeDirUtil {

    final static Logger logger = LoggerFactory.getLogger(MakeDirUtil.class);

    public static void mkDir(String path) {

        File file;
        try {
            file = new File(path);
            if (!file.exists()) {
                boolean res = file.mkdirs();
                if (res) {
                    logger.info("创建目录 {} 成功", path);
                }
            } else {
                logger.info("目录 {} 存在", path);
            }
        } catch (Exception ignored) {
            logger.info("创建目录 {} 发生错误", path);
        }
    }

}