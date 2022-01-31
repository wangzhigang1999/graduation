package com.bupt.graduation.service.impl;

import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.entity.Resp;
import com.bupt.graduation.service.ImageUploadService;
import com.bupt.graduation.utils.CommonQueryUtil;
import com.bupt.graduation.utils.ImageSaveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service("local")
public class LocalUploadServiceImpl implements ImageUploadService {
    @Autowired
    PhotoDao photoDao;
    @Autowired
    CommonQueryUtil commonQueryUtil;

    @Override
    public Object upload(String uuid, MultipartFile file) {
        try {

            if (file == null) {
                throw new NullPointerException("The uploaded file is empty, please check and try again!");
            }

            if (commonQueryUtil.notExist(uuid)) {
                return new Resp(false, 200, "Group photo does not exist", null);
            }


            // 给文件生成一个名称,这个文件是不会被删除的,因此不将它添加到 redis 中
            String fileName = UUID.randomUUID().toString();


            // 提交到线程池中
            ImageSaveUtil.add(file, fileName);

            // 修改数据库数据
            Integer res = photoDao.uploadBackgroundImg(uuid, fileName + ".jpeg");

            if (res == 1) {
                return new Resp(true, 200, "success", null);
            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            return new Resp(false, 200, "Failed to upload background!", null);
        }
    }
}
