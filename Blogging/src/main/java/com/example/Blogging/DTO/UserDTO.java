package com.example.Blogging.DTO;

import com.example.Blogging.Models.Posts;
import com.example.Blogging.Models.Role;
import com.example.Blogging.Models.UserProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDTO {

    private UUID id;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int failedLoginAttempt;
    private boolean accountLocked;
    private LocalDateTime createdAt;
    private LocalDateTime timeStamp;

}
