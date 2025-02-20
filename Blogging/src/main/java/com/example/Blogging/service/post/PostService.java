package com.example.Blogging.service.post;

import com.example.Blogging.DAO.request.PostRequest;
import com.example.Blogging.DAO.response.PostResponse;
import com.example.Blogging.FeignImageProxy.ImageService;
import com.example.Blogging.Models.Posts;
import com.example.Blogging.Models.User;
import com.example.Blogging.repo.PostRepo;
import com.example.Blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final ImageService imageService;

    @Autowired
    public PostService(PostRepo postRepo, UserRepo userRepo, ImageService imageService) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.imageService = imageService;
    }

    public ResponseEntity<String> createPost(PostRequest postRequest , UUID userId){
        Optional<User> user = userRepo.findById(userId);

        if(user.isPresent()){
            Posts posts = new Posts();
            posts.setDescription(postRequest.getDescription());
            posts.setTitle(postRequest.getTitle());
            user.get().setPostsData(posts);
            posts.setUser(user.get());
            userRepo.save(user.get());
            return new ResponseEntity<>("Post Created Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Post not created ",HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> updatePost(PostResponse postResponse){
        Optional<Posts> posts = postRepo.findById(postResponse.getId());

        if(posts.isPresent()){
            posts.get().setTitle(postResponse.getTitle());
            posts.get().setDescription(postResponse.getDescription());
            postRepo.save(posts.get());
            return new ResponseEntity<>("Post Update Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Posts doesn't exist . Please create before update" , HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<List<PostResponse>> getAllApprovedPosts(){

        List<Posts> posts = postRepo.findAllApprovedPosts();

        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());

        return !postResponses.isEmpty()
                ? new ResponseEntity<>(postResponses , HttpStatus.OK)
                : new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<Posts> posts = postRepo.findAllPostDesc();

        if(posts.isEmpty()){
            return new ResponseEntity<>(Collections.emptyList(),HttpStatus.NO_CONTENT);
        }

        List<PostResponse> postResponses = posts.stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());

        return !postResponses.isEmpty()
                ? new ResponseEntity<>(postResponses , HttpStatus.OK)
                : new ResponseEntity<>(Collections.emptyList() , HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<List<PostResponse>> getPostsOfAUser(UUID userId){

        List<Posts> posts = postRepo.findAllPostByUserId(userId);

        if(posts.isEmpty()){
            return new ResponseEntity<>(Collections.emptyList() , HttpStatus.BAD_REQUEST);
        }

        List<PostResponse> list = posts
                .stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());

        return list != null ? new ResponseEntity<>(list , HttpStatus.OK) : new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> uploadPostImage(MultipartFile image , UUID postId){
        Optional<Posts> posts = postRepo.findById(postId);

        if(posts.isPresent()){
            if(posts.get().getImage() == null){
                String profile = "post_profile";
                ResponseEntity<String> data = imageService.uploadImage(posts.get().getUser().getEmail(),profile,image);
                if(data.getBody() != null && data.getStatusCode() == HttpStatus.OK){
                    UUID imageId = UUID.fromString(data.getBody());
                    posts.get().setImage(imageId);
                    postRepo.save(posts.get());
                    return new ResponseEntity<>("Image upload Success" , HttpStatus.OK);
                }
                return new ResponseEntity<>(data.getBody(),data.getStatusCode());
            }
            return new ResponseEntity<>("Image already Exist . try to update it",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Post doesn't exist . Please first create post",HttpStatus.NOT_ACCEPTABLE);

    }

    private PostResponse mapToPostResponse(Posts post) {
        String imageBase64 = convertImageToBase64(post.getImage());
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                imageBase64,
                post.getDescription(),
                post.isApproved()
        );
    }

    private String convertImageToBase64(UUID imageId) {
        if (imageId == null) {
            return null;
        }
        try {
            ResponseEntity<Resource> responseEntity = imageService.getUserProfileImage(imageId);
            Resource resource = responseEntity.getBody();

            if (resource != null) {
                try (InputStream inputStream = resource.getInputStream()) {
                    byte[] imageBytes = inputStream.readAllBytes();
                    return Base64.getEncoder().encodeToString(imageBytes);
                }
            }
        } catch (IOException e) {
            System.out.println("Image not found");
        }
        return null;
    }

}
