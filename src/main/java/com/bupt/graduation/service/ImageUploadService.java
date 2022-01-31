package com.bupt.graduation.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author wanz
 */
public interface ImageUploadService {
    Object upload(String uuid, MultipartFile file);
}
