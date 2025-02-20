package com.example.Blogging.controller;

import com.example.Blogging.DAO.response.PostResponse;
import com.example.Blogging.service.admin.AdminService;
import com.example.Blogging.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final PostService postService;

    @Autowired
    public AdminController(AdminService adminService, PostService postService) {
        this.adminService = adminService;
        this.postService = postService;
    }

    @GetMapping("/posts/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return postService.getAllPosts();
    }

    @PutMapping("/post")
    public ResponseEntity<String> postApproveOrReject(@RequestBody PostResponse post){
        return adminService.approveOrRejectPost(post);
    }


}
