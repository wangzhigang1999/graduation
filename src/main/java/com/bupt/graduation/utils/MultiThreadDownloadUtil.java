package com.bupt.graduation.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static com.bupt.graduation.utils.Config.BASE_DIR;

public class MultiThreadDownloadUtil {
    static class MultiThreadDownload implements Callable<Boolean> {
        private final String imgUrl;
        private final String filePath;
        CountDownLatch latch;

        public MultiThreadDownload(String imgUrl, String filePath, CountDownLatch latch) {
            this.imgUrl = imgUrl;
            this.filePath = filePath;
            this.latch = latch;
        }

        @Override
        public Boolean call() {
            SimpleDownLoadUtil.download(filePath, imgUrl);
            if (latch != null) {
                latch.countDown();
            }
            return Files.exists(Path.of(filePath));
        }

    }

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());


    public static void download(String[] imgUrls, String[] filePaths) throws InterruptedException, ExecutionException {
        int len = imgUrls.length;

        List<Future<Boolean>> res = new ArrayList<>(len);

        CountDownLatch latch = new CountDownLatch(len);
        for (int i = 0; i < len; i++) {
            Future<Boolean> future = pool.submit(new MultiThreadDownload(imgUrls[i], filePaths[i], latch));
            res.add(future);
        }

        latch.await();

        for (int i = 0; i < res.size(); i++) {
            if (!res.get(i).get()) {
                SimpleDownLoadUtil.download(filePaths[i], imgUrls[i]);
            }
        }
    }


    public static String[] download(String[] imgUrls) throws InterruptedException, ExecutionException {
        int len = imgUrls.length;
        String[] filePaths = new String[len];
        for (int i = 0; i < filePaths.length; i++) {
            UUID uid = UUID.randomUUID();
            String savePath = BASE_DIR + uid + ".jpg";
            filePaths[i] = savePath;
        }
        download(imgUrls, filePaths);
        return filePaths;
    }

}

