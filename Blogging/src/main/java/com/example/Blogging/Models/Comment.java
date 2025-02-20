package com.example.Blogging.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id" , updatable = false,nullable = false,unique = true)
    private UUID id;

    @NotBlank(message = "Comment can't be blank")
    @Column(name = "other_user_comments" , length = 100)
    private String otherUserComment;

    @Column(name = "comment_time",nullable = false,updatable = false)
    private LocalDateTime timeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id",nullable = false)
    private Posts posts;

    @PrePersist
    public void commentTimeStamp(){
        timeStamp = LocalDateTime.now();
    }

}
