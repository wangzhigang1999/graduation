package com.bupt.graduation.service.impl;

import com.bupt.graduation.service.ImageSegService;
import com.bupt.graduation.utils.ImageUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service("modnet")
public class LocalImageSegServiceImpl implements ImageSegService {
    @Value("${segService.timeout}")
    public int DEFAULT_TIME_OUT;
    @Value("${segService.url}")
    public String URL;

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadUtil.class);

    @Override
    public String seg(MultipartFile file) {
        try {
            return doPostFileRequest(URL, file.getInputStream());
        } catch (IOException e) {
            logger.error("matting 失败， 原因是 {}", e.getLocalizedMessage());
        }
        return null;
    }

    protected String doPostFileRequest(String url, InputStream fis) {
        try {
            String unique = UUID.randomUUID().toString();
            logger.info("准备matting... url={} unique={}", url, unique);
            Connection conn = Jsoup.connect(url);
            conn.data("file", "file", fis);
            Connection.Response response = conn.method(Connection.Method.POST).execute();
            logger.info("matting 完成... return={} unique={}", response.body(), unique);
            return response.body();
        } catch (Exception e) {
            logger.error("matting 失败， 原因是 {}", e.getLocalizedMessage());
            return null;
        }

    }

}
