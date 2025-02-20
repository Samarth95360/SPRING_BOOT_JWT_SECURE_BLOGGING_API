package com.example.Blogging.DAO.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentResponse {

    private UUID id;

    private String otherUserComment;

    private LocalDateTime timeStamp;


}
