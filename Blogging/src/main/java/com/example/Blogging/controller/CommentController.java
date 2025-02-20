package com.example.Blogging.controller;

import com.example.Blogging.DAO.request.CommentRequest;
import com.example.Blogging.DAO.response.CommentResponse;
import com.example.Blogging.service.Comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<String> createCommentForAPost(@PathVariable(name = "postId") UUID postId, @RequestBody CommentRequest commentRequest){
        return commentService.createComment(postId , commentRequest);
    }

}
