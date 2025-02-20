package com.integration.imageStorage.dirCreation.image.service;

import com.integration.imageStorage.dirCreation.image.entity.Image;
import com.integration.imageStorage.dirCreation.image.repo.ImageRepo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepo imageRepo;

    public ImageService(ImageRepo imageRepo){
        this.imageRepo = imageRepo;
    }

    public ResponseEntity<String> uploadImage(String userName, String fullPath, MultipartFile multipartFile) throws IOException {
        String finalImagePath = fullPath+"/"+multipartFile.getOriginalFilename();
        Image image = new Image();
        image.setImageName(multipartFile.getOriginalFilename());
        image.setImageType(multipartFile.getContentType());
        image.setImageFullPath(finalImagePath);
        image.setUserName(userName);
        imageRepo.save(image);
        multipartFile.transferTo(new File(finalImagePath));
        String imageId = imageRepo.findByImageFullPath(finalImagePath).getId().toString();
        if (imageId != null){
            return new ResponseEntity<>(imageId, HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    public Resource displayImages(UUID imageId) throws IOException {

        Optional<Image> image = imageRepo.findById(imageId);

        if (image.isEmpty()) {
            return null; // No image found for the given userName
        }

        String path = image.get().getImageFullPath();
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            throw new IOException("File not found: " + path);
        }

        return resource;
    }

}
