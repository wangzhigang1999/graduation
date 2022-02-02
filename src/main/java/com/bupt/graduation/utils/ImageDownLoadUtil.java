package com.bupt.graduation.utils;


import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.*;


/**
 * @author wangzhigang
 */
public class ImageDownLoadUtil {
    public static void downImages(String filePath, String imgUrl) {
        try {
            File file = new File(filePath);
            URL url = new URL(imgUrl);
            FileUtils.copyURLToFile(url, file);
        } catch (Exception ignore) {
        }

    }

    public static void main(String[] args) {
        Integer a = 1;

    }
}