package com.integration.imageStorage.dirCreation.image.controller;

import com.integration.imageStorage.dirCreation.fileCreation.service.FileService;
import com.integration.imageStorage.dirCreation.image.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class ImageController {

    private final ImageService imageService;
    private final FileService fileService;

    public ImageController(ImageService imageService,FileService fileService){
        this.fileService = fileService;
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("userName") String userEmail,@RequestParam("profile") String profile, @RequestPart("multiPartFile")MultipartFile multipartFile) throws IOException {
        String fullPath = fileService.createDir(userEmail,profile);
        if(fullPath!=null){
            return imageService.uploadImage(userEmail,fullPath,multipartFile);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Try Again Please With Other UserName");

    }

    @GetMapping("/image")
    public ResponseEntity<Resource> displayImage(@RequestParam(name = "imageId") UUID imageId) throws IOException {

        Resource imageData = imageService.displayImages(imageId);

        if (imageData == null || !imageData.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Determine the content type dynamically if needed
        String contentType = Files.probeContentType(Paths.get(imageData.getFile().getAbsolutePath()));
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default content type
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageData);
    }



}
