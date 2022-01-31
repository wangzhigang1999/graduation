package com.bupt.graduation.service.impl;

import com.bupt.graduation.service.ImageSegService;
import com.bupt.graduation.utils.ImageSegmentationFull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("baidu")
public class BaiduImageSegServiceImpl implements ImageSegService {
    @Override
    public String seg(MultipartFile file) throws IOException {
        return ImageSegmentationFull.getImgUrl(file.getBytes());
    }
}
