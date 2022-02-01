package com.bupt.graduation.service.impl;

import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.entity.Resp;
import com.bupt.graduation.service.ImageUploadService;
import com.bupt.graduation.utils.ImageUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("remote")
public class RemoteUploadServiceImpl implements ImageUploadService {
    @Autowired
    PhotoDao photoDao;
    private static final Logger logger = LoggerFactory.getLogger(ImageUploadUtil.class);

    @Override
    public Object upload(String uuid, MultipartFile file) {


        if (file == null) {
            logger.error("获取的二进制文件是null  uuid={}", uuid);
            return new Resp(false, 200, "上传文件失败！", null);
        }

        String url;
        try {
            url = ImageUploadUtil.saveOnline(file);
        } catch (IOException e) {
            logger.error("上传文件失败  uuid={} 原因是:{}", uuid, e.getLocalizedMessage());
            return new Resp(false, 200, "上传文件失败！", null);
        }

        // 修改数据库数据
        Integer res = photoDao.uploadBackgroundImg(uuid, url);

        if (res == 1) {
            return new Resp(true, 200, "success", null);
        } else {
            logger.error("更新数据库失败  uuid={}", uuid);
            return new Resp(false, 200, "更新数据库失败！请重试！", null);
        }

    }


}
