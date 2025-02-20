package com.example.Blogging.FeignImageProxy;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImageService {

    private final ImageProxy imageProxy;

    @Autowired
    public ImageService(ImageProxy imageProxy) {
        this.imageProxy = imageProxy;
    }

    @CircuitBreaker(name = "imageProxy" , fallbackMethod = "uploadImageFallback")
    public ResponseEntity<String> uploadImage(String userName , String profile, MultipartFile multipartFile){
        return imageProxy.uploadImage(userName, profile, multipartFile);
    }

    @CircuitBreaker(name = "imageProxy", fallbackMethod = "getImageFallback")
    public ResponseEntity<Resource> getUserProfileImage(UUID imageId){
        return imageProxy.getUserProfileImage(imageId);
    }

    public ResponseEntity<String> uploadImageFallback(String userName, String profile, MultipartFile multipartFile, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Image upload service is currently unavailable. Please try again later.");
    }

    public ResponseEntity<Resource> getImageFallback(UUID imageId, Throwable throwable) {
        System.err.println("Fallback triggered for getUserProfileImage with ID: " + imageId + ". Reason: " + throwable.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}
