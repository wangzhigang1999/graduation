package com.bupt.graduation.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图像分割
 */
public interface ImageSegService {
    String seg(MultipartFile file);
}
