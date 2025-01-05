package com.example.Blogging.controller;

import com.example.Blogging.DAO.request.PostRequest;
import com.example.Blogging.DAO.response.PostResponse;
import com.example.Blogging.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("In controller");
        return this.postService.createPost(postRequest, userId);
    }

    @GetMapping("/post/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostResponse>> getPostsOfAUser(@AuthenticationPrincipal UUID userId){
        return postService.getPostsOfAUser(userId);
    }

}
