package com.example.Blogging.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id" , unique = true,nullable = false , updatable = false)
    private UUID id;

    @Column(name = "image_id",unique = true)
    private UUID imageId; // Store the Id of the image

    @NotBlank(message = "Bio can't be blank")
    @Column(name = "user_bio" , columnDefinition = "TEXT" , length = 100)
    private String bio;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "profile_created" , nullable = false,updatable = false)
    private LocalDateTime timeStamp;

    @Column(name = "address")
    private String address;

    @OneToOne(mappedBy = "userProfile",fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void profileCreatedAt(){
        timeStamp = LocalDateTime.now();
    }

}
