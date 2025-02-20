package com.example.Blogging.DAO.response;

import com.example.Blogging.Models.Posts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private UUID id;

    private String title;

    private String image;

    private String description;

    private boolean approved;

}
