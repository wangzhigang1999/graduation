package com.bupt.graduation.utils; /**
 * @Author DemoRookie
 * @Version 1.0
 */

import java.io.*;
import java.net.*;


public class ImageDownLoadUtil {


    public static void downImages(String filePath, String imgUrl) {
        try {
            String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);//截取图片文件名
            /*
             * 文件名里面可能有中文或者空格，所以这里要进行处理，
             * 但空格又会被URLEncoder转义为加号，
             * 因此要将加号转化为UTF-8格式的%20
             * */
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        File file = new File(filePath);//写出路径
        InputStream inStream;

        try {
            URL url = new URL(imgUrl);// 构造URL
            URLConnection con = url.openConnection();// 打开连接
            inStream = con.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();//中转站，现将图片数据放到outStream中
            byte[] buf = new byte[1024];
            int len;
            while ((len = inStream.read(buf)) != -1) {
                outStream.write(buf, 0, len);
            }
            inStream.close();
            outStream.close();
            FileOutputStream op = new FileOutputStream(file);//图片下载的位置
            op.write(outStream.toByteArray());
            op.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}