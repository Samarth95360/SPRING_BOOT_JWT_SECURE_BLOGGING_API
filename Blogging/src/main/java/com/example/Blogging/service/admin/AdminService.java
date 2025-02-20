package com.example.Blogging.service.admin;

import com.example.Blogging.DAO.response.PostResponse;
import com.example.Blogging.Models.Posts;
import com.example.Blogging.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final PostRepo postRepo;

    @Autowired
    public AdminService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }


    public ResponseEntity<String> approveOrRejectPost(PostResponse postResponse){
        Optional<Posts> posts = postRepo.findById(postResponse.getId());

        if (posts.isPresent()){
            posts.get().setApproved(postResponse.isApproved());
            postRepo.save(posts.get());
            return postResponse.isApproved()
                    ? new ResponseEntity<>("Post Approved",HttpStatus.OK)
                    : new ResponseEntity<>("Post Rejected",HttpStatus.OK);
        }
        return new ResponseEntity<>("Post doesn't exist",HttpStatus.BAD_REQUEST);

    }

}
