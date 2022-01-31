package com.bupt.graduation.service.impl;

import com.bupt.graduation.service.ImageSegService;
import com.bupt.graduation.utils.ImageSaveUtil;
import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Service("modnet")
public class LocalImageSegServiceImpl implements ImageSegService {
    @Value("${segService.timeout}")
    private int DEFAULT_TIME_OUT;
    @Value("${segService.url}")
    private String URL;

    @Override
    public String seg(MultipartFile file) throws IOException {
        return doPostFileRequest(URL, file.getInputStream());
    }

    protected String doPostFileRequest(String url, InputStream fis) {
        try {
            Connection conn = Jsoup.connect(url);

            conn.data("file", "file", fis);
            Connection.Response response = conn.method(Connection.Method.POST).timeout(DEFAULT_TIME_OUT).execute();
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
