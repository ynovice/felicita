package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Image;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image uploadImage(@NonNull MultipartFile multipartFile);

    byte[] getContentsById(@NonNull Long id);

    void delete(@NonNull Image image);
}
