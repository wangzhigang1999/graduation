package com.bupt.graduation.utils;

import org.apache.commons.io.file.PathUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 在特定的时机删除不再使用的照片
 * 什么是特定的时机?管理员将合照发布之后就可以进行删除了,但是不能删除最终生成的合照信息
 *
 * @author wangz
 */
public class ImageDeleteUtil {

    private final static String BASE_DIR = System.getProperties().getProperty("user.dir") + "/IMG/";


    private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(
            1,
            Runtime.getRuntime().availableProcessors() * 2,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void delete(Set<String> images) {
        POOL_EXECUTOR.submit(() -> add(images)
        );
    }

    private static void add(Set<String> images) {
        images.forEach((o) -> {
            try {
                PathUtils.delete(Paths.get(BASE_DIR + o));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }


}



