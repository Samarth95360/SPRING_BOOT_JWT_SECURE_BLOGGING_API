package com.example.Blogging.service.post;

import com.example.Blogging.DAO.request.PostRequest;
import com.example.Blogging.DAO.response.PostResponse;
import com.example.Blogging.Models.Posts;
import com.example.Blogging.Models.User;
import com.example.Blogging.repo.PostRepo;
import com.example.Blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;

    @Autowired
    public PostService(PostRepo postRepo, UserRepo userRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    public ResponseEntity<String> createPost(PostRequest postRequest , UUID userId){
        Optional<User> user = userRepo.findById(userId);

        if(user.isPresent()){
            System.out.println("In service");
            Posts posts = new Posts();
            posts.setDescription(postRequest.getDescription());
            posts.setTitle(postRequest.getTitle());
            user.get().setPostsData(posts);
            posts.setUser(user.get());
            userRepo.save(user.get());
//            postRepo.save(posts);
            return new ResponseEntity<>("Post Created Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Post not created ",HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<PostResponse> posts = postRepo.findAll(Sort.by(Sort.Direction.DESC , "timeStamp"))
                .stream()
                .map(
                        post -> new PostResponse(post.getId() ,post.getTitle(), post.getImage(), post.getDescription())
                )
                .toList();
        return posts != null ? new ResponseEntity<>(posts , HttpStatus.OK) : new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<PostResponse>> getPostsOfAUser(UUID userId){
        List<PostResponse> list = postRepo.findAllPostByUserId(userId)
                .stream()
                .map(
                        post -> new PostResponse(post.getId() ,post.getTitle(), post.getImage(),post.getDescription())
                )
                .toList();

        return list != null ? new ResponseEntity<>(list , HttpStatus.OK) : new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

}
