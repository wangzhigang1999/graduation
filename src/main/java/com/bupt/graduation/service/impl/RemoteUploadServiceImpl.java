package com.bupt.graduation.service.impl;

import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.entity.Resp;
import com.bupt.graduation.service.ImageUploadService;
import com.bupt.graduation.utils.CommonQueryUtil;
import com.bupt.graduation.utils.ImageSaveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("remote")
public class RemoteUploadServiceImpl implements ImageUploadService {
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


            String url = ImageSaveUtil.saveOnline(file);


            // 修改数据库数据
            Integer res = photoDao.uploadBackgroundImg(uuid, url);

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
