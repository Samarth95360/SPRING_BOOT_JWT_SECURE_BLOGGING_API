package com.example.Blogging.service.Comment;

import com.example.Blogging.DAO.request.CommentRequest;
import com.example.Blogging.Models.Comment;
import com.example.Blogging.Models.Posts;
import com.example.Blogging.repo.CommentRepo;
import com.example.Blogging.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo, PostRepo postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    public ResponseEntity<String> createComment(UUID postId , CommentRequest commentRequest){
        System.out.println("In Service");
        Optional<Posts> posts = postRepo.findById(postId);

        if (posts.isPresent()){
            Comment comment = new Comment();
            comment.setOtherUserComment(commentRequest.getOtherUserComment());
            posts.get().setComments(comment);
            comment.setPosts(posts.get());
            postRepo.save(posts.get());
            return new ResponseEntity<>("Comment Added Success" , HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to add comment" , HttpStatus.BAD_REQUEST);

    }

}
