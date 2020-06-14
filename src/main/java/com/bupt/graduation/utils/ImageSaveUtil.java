package com.bupt.graduation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 将二进制形式的图片转存到本地
 * 线程池
 *
 * @author wangz
 */
public class ImageSaveUtil {

    private final static String BG_PATH = System.getProperties().getProperty("user.dir") + "/IMG";

    private static final Logger logger = LoggerFactory.getLogger(ImageSaveUtil.class);


    static {
        MakeDirUtil.mkDir(BG_PATH);
    }

    private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(
            1,
            Runtime.getRuntime().availableProcessors() * 2,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void add(MultipartFile file, String fileName) {
        logger.info("Submit tasks to the thread pool");
        POOL_EXECUTOR.execute(() -> save(file, fileName));
    }

    private static void save(MultipartFile file, String fileName) {
        try {

            logger.info("Start dumping files");
            byte[] bytes = file.getBytes();
            Path path = Paths.get(BG_PATH + "/" + fileName + ".jpeg");
            Files.write(path, bytes);
            logger.info("Dump file succeeded");

        } catch (IOException e) {
            logger.error("Dump file failed!");
        }
    }
}
