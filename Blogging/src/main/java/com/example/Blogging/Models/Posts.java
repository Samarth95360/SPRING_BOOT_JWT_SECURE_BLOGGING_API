package com.example.Blogging.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id" , unique = true , nullable = false , updatable = false)
    private UUID id;

    @NotBlank(message = "Title can't be left blank")
    @Column(name = "title" , nullable = false , unique = true)
    private String title;

    @Column(name = "post_image_id",unique = true)
    private UUID image;

    @NotBlank(message = "Description can't be blank")
    @Size(min = 10,max = 300,message = "Can't exceed limit of 10 and 300")
    @Column(name = "description" , length = 300)
    private String description;

    @Column(name = "post_creation_time_stamp",updatable = false)
    private LocalDateTime timeStamp;

    @Column(name = "approved_status" ,nullable = false)
    private boolean approved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @OneToMany(mappedBy = "posts" , cascade = CascadeType.ALL , fetch = FetchType.LAZY , orphanRemoval = true)
    private List<Comment> comments;

    @PrePersist
    public void postCreatedAt(){
        timeStamp = LocalDateTime.now();
    }

    public void setComments(Comment comment){
        if(comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

}
