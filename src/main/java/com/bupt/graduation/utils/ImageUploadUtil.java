package com.bupt.graduation.utils;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 将二进制形式的图片转存到云端
 *
 * @author wangzhigang
 */
public class ImageUploadUtil {

    private final static String BG_PATH = System.getProperties().getProperty("user.dir") + "/IMG";

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadUtil.class);


    public static final String UPLOAD_TARGET;

    public static final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        System.out.println(pool.getPoolSize());
        MakeDirUtil.mkDir(BG_PATH);
        Map<String, String> map = System.getenv();
        UPLOAD_TARGET = map.get("HTTP_UPLOAD_PATH");
    }

    public static String saveOnline(MultipartFile file) throws IOException {
        return postFileRequest(file.getInputStream());
    }

    public static String saveOnline(InputStream file) {
        return postFileRequest(file);
    }

    private static String postFileRequest(InputStream fis) {
        try {
            logger.info("connect url {}", ImageUploadUtil.UPLOAD_TARGET);
            Connection conn = Jsoup.connect(ImageUploadUtil.UPLOAD_TARGET);
            conn.data("file", "file", fis);
            Connection.Response response = conn.method(Connection.Method.POST).execute();
            Map map = new Gson().fromJson(response.body(), Map.class);
            return (String) map.get("fileName");
        } catch (Exception e) {
            logger.error("上传文件失败 url:{}", ImageUploadUtil.UPLOAD_TARGET);
            return null;
        }

    }
}
