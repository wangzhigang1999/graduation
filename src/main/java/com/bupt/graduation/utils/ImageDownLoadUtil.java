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
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            downImages("./test.jpg", "https://www.bupt.site/image/1643708811.5312743.jpg");
            System.out.println(System.currentTimeMillis() - start);
        }

    }
}