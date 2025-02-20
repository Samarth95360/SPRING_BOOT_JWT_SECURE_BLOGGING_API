package com.example.Blogging.controller;

import com.example.Blogging.DAO.request.PostRequest;
import com.example.Blogging.DAO.response.PostResponse;
import com.example.Blogging.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<String> createPost(@AuthenticationPrincipal UUID userId ,@RequestBody PostRequest postRequest){
        return this.postService.createPost(postRequest, userId);
    }

    @PutMapping("/post")
    public ResponseEntity<String> updatePost(@RequestBody PostResponse postResponse){
        return this.postService.updatePost(postResponse);
    }

    @GetMapping("/post/all")
    public ResponseEntity<List<PostResponse>> getAllPostsThatAreApproved(){
        return postService.getAllApprovedPosts();
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostResponse>> getPostsOfAUser(@AuthenticationPrincipal UUID userId){
        return postService.getPostsOfAUser(userId);
    }

    @PostMapping("/post-image")
    public ResponseEntity<String> uploadPostImage(@RequestParam("multiPartFile") MultipartFile multipartFile , @RequestParam("postId") UUID postId){
        return postService.uploadPostImage(multipartFile, postId);
    }
}
