package com.example.Blogging.DAO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostRequest {

    private UUID postId;

    @NotBlank(message = "Title can't be left blank")
    private String title;

    @NotBlank(message = "Description can't be blank")
    @Size(min = 10,max = 300,message = "Can't exceed limit of 10 and 300")
    private String description;
}
