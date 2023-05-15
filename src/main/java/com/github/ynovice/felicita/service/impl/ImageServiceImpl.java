package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.config.ImgProperties;
import com.github.ynovice.felicita.exception.BadRequestException;
import com.github.ynovice.felicita.exception.InternalServerError;
import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.entity.Image;
import com.github.ynovice.felicita.repository.ImageRepository;
import com.github.ynovice.felicita.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    public static Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "webp");

    private final ImgProperties imgProperties;

    private final ImageRepository imageRepository;

    @Override
    public Image uploadImage(MultipartFile multipartFile) {

        byte[] fileBytes;
        try {
            fileBytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Выбранное вами изображение невалидно");
        }

        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if(!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new BadRequestException("Выбранное вами изображение имеет недопустимый формат");
        }

        Image image = new Image();
        image.setExtension(fileExtension);
        imageRepository.saveAndFlush(image);

        Path fileNameAndPath = Paths.get(
                imgProperties.getUploadDirectory(),
                image.getId() + "." + fileExtension);

        try {
            Files.write(fileNameAndPath, fileBytes);
        } catch (IOException e) {
            imageRepository.delete(image);
            throw new InternalServerError("На сервере произошла ошибка при попытке сохранить изображение");
        }

        return image;
    }

    @Override
    public byte[] getContentsById(Long id) {

        Image image = imageRepository.findById(id).orElseThrow(NotFoundException::new);

        Path fileNameAndPath = Paths.get(imgProperties.getUploadDirectory(), id + "." + image.getExtension());

        try {
            return Files.readAllBytes(fileNameAndPath);
        } catch (IOException e) {
            throw new InternalServerError("На сервере произошла ошибка при попытке достать нужное изображение");
        }
    }
}
