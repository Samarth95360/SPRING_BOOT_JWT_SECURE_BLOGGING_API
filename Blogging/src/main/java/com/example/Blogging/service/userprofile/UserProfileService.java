package com.example.Blogging.service.userprofile;

import com.example.Blogging.DAO.request.UserProfileRequest;
import com.example.Blogging.DAO.response.UserProfileResponse;
import com.example.Blogging.FeignImageProxy.ImageProxy;
import com.example.Blogging.FeignImageProxy.ImageService;
import com.example.Blogging.Models.User;
import com.example.Blogging.Models.UserProfile;
import com.example.Blogging.repo.UserProfileRepo;
import com.example.Blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserRepo userRepo;

    private final UserProfileRepo userProfileRepo;

    private final ImageProxy imageProxy;

    private final ImageService imageService;

    @Autowired
    public UserProfileService(UserRepo userRepo, UserProfileRepo userProfileRepo, ImageProxy imageProxy, ImageService imageService){
        this.userRepo = userRepo;
        this.userProfileRepo = userProfileRepo;
        this.imageProxy = imageProxy;
        this.imageService = imageService;
    }

    public ResponseEntity<String> createUserProfile(UUID userId, UserProfileRequest userProfile){
        Optional<User> user = userRepo.findById(userId);

        if(user.isPresent()){
            UserProfile userProfile1 = new UserProfile();
            userProfile1.setBio(userProfile.getBio());
            userProfile1.setAddress(userProfile.getAddress());
            userProfile1.setBirthDate(userProfile.getBirthDate());
            user.get().setUserProfile(userProfile1);
            userProfile1.setUser(user.get());
//            userProfileRepo.save(userProfile1);
            userRepo.save(user.get());
            return new ResponseEntity<>("Profile Created Success" , HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid User",HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> updateProfile(UUID userId , UserProfileRequest userProfileRequest){
        Optional<User> user = userRepo.findById(userId);

        if(user.isPresent()){
            UserProfile userProfile = user.get().getUserProfile();
            if(userProfile != null){
                userProfile.setBio(userProfileRequest.getBio());
                userProfile.setAddress(userProfileRequest.getAddress());
                userProfile.setBirthDate(userProfileRequest.getBirthDate());
                userProfileRepo.save(userProfile);
                return new ResponseEntity<>("Updates Success" , HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("User Doesn't Exist" , HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> uploadImageForUserProfile(MultipartFile multipartFile,UUID userId){
        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){
            if(user.get().getUserProfile()!=null){
                if(user.get().getUserProfile().getImageId() == null){
                    String profile = "user_profile";
//                    ResponseEntity<String> data = imageProxy.uploadImage(user.get().getEmail(),profile,multipartFile);
                    ResponseEntity<String> data = imageService.uploadImage(user.get().getEmail(),profile,multipartFile);
                    if(data.getBody() != null && data.getStatusCode() == HttpStatus.OK){
                        UUID imageId = UUID.fromString(data.getBody());
                        UserProfile userProfile = userRepo.findById(userId).get().getUserProfile();
                        if (userProfile != null) {
                            userProfile.setImageId(imageId);
                            userProfileRepo.save(userProfile);
                            return new ResponseEntity<>("Image Uploaded Success", HttpStatus.OK);
                        }
                        return new ResponseEntity<>("Failed to connect" , HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>(data.getBody(),data.getStatusCode());
                }
                return new ResponseEntity<>("Image Already Exist please go for update" , HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>("User Profile Doesn't Exist" , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User Doesn't Exist" , HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<UserProfileResponse> getUserProfile(UUID userId){
        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){
            if(user.get().getUserProfile()!=null){
                UserProfile userProfile = user.get().getUserProfile();

                UserProfileResponse response = new UserProfileResponse();
                response.setId(userProfile.getId());
                response.setBio(userProfile.getBio());
                response.setAddress(userProfile.getAddress());
                response.setBirthDate(userProfile.getBirthDate());
                if (userProfile.getImageId() != null) {
                    ResponseEntity<Resource> responseEntity = imageService.getUserProfileImage(userProfile.getImageId());
                    Resource resource = responseEntity.getBody();
                    if (resource != null) {
                        try (InputStream inputStream = resource.getInputStream()) {
                            byte[] bytes = inputStream.readAllBytes();
                            String base64Image = Base64.getEncoder().encodeToString(bytes);
                            response.setImage(base64Image);
                            return new ResponseEntity<>(response,HttpStatus.OK);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return new ResponseEntity<>(responseEntity.getStatusCode());
                }
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            return new ResponseEntity<>(new UserProfileResponse(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserProfileResponse(),HttpStatus.NOT_FOUND);
    }

}
