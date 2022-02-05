package com.bupt.graduation.utils;

import java.io.File;

/**
 * 创建文件夹的工具类
 *
 * @author wangz
 */
public class MakeDirUtil {

    public static void mkDir(String path) {

        File file;
        try {
            file = new File(path);
            if (!file.exists()) {
                boolean res = file.mkdirs();
                System.out.println(res);
            }
        } catch (Exception ignored) {
        }
    }

}