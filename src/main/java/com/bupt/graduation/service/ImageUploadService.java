package com.bupt.graduation.service;

import com.bupt.graduation.entity.Resp;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wanz
 */
public interface ImageUploadService {
    Resp upload(String uuid, MultipartFile file);
}
