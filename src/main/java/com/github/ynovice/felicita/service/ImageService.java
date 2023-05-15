package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image uploadImage(MultipartFile multipartFile);

    byte[] getContentsById(Long id);
}
