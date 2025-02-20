package com.example.Blogging.controller;

import com.example.Blogging.DAO.request.UserProfileRequest;
import com.example.Blogging.DAO.response.UserProfileResponse;
import com.example.Blogging.service.userprofile.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    @PostMapping("/user-profile")
    public ResponseEntity<String> createProfileForUser(@RequestBody UserProfileRequest userProfile, @AuthenticationPrincipal UUID userId){
        return userProfileService.createUserProfile(userId,userProfile);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadImageForUserProfile(@RequestParam("multiPartFile") MultipartFile multipartFile , @AuthenticationPrincipal UUID userId){
        return userProfileService.uploadImageForUserProfile(multipartFile,userId);
    }

    @PutMapping("/user-profile")
    public ResponseEntity<String> updateProfileForUser(@RequestBody UserProfileRequest userProfileRequest , @AuthenticationPrincipal UUID userId){
        return userProfileService.updateProfile(userId, userProfileRequest);
    }

    @GetMapping("/user-profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal UUID userId){
        return userProfileService.getUserProfile(userId);
    }

}
