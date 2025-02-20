package com.example.Blogging.FeignImageProxy;


import com.example.Blogging.config.FeignConfig;
import com.example.Blogging.config.FeignMultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(name = "imageStorage-dirCreation", configuration = {FeignMultipartSupportConfig.class, FeignConfig.class})
public interface ImageProxy {

    @PostMapping( value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadImage(
            @RequestParam("userName") String userName,
            @RequestParam("profile") String profile,
            @RequestPart("multiPartFile") MultipartFile multipartFile
    );

    @GetMapping(value = "/image")
    ResponseEntity<Resource> getUserProfileImage(@RequestParam("imageId") UUID imageId);

}
