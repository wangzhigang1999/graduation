package com.bupt.graduation.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 图像分割
 */
public interface ImageMattingService {
    String matting(MultipartFile file);
}
