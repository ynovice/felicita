package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ImageDto;
import com.github.ynovice.felicita.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<ImageDto> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        return ResponseEntity.ok(
                ImageDto.fromEntity(imageService.uploadImage(multipartFile))
        );
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        imageService.deleteById(id);
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getContentsById(@PathVariable Long id) {
        return imageService.getContentsById(id);
    }
}
