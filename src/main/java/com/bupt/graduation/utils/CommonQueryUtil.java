package com.bupt.graduation.utils;

import com.bupt.graduation.dao.PhotoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonQueryUtil {

    final PhotoDao photoDao;

    public CommonQueryUtil(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    public boolean notExist(String uuid) {
        Long idByUuid = photoDao.getIdByUuid(uuid);
        return idByUuid == null;
    }
}
