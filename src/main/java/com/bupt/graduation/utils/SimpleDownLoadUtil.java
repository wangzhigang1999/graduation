package com.bupt.graduation.utils;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;


/**
 * 一个简单的下载器
 *
 * @author wangzhigang
 */
public class SimpleDownLoadUtil {

    public static void download(String savePath, String targetUrl) {
        try {
            File file = new File(savePath);
            URL url = new URL(targetUrl);
            FileUtils.copyURLToFile(url, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}