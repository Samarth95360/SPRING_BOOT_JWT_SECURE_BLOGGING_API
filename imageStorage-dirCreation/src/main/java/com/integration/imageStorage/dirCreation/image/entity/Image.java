package com.integration.imageStorage.dirCreation.image.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id" , unique = true,nullable = false,updatable = false)
    private UUID id;

    @Column(name = "user_name" , nullable = false,length = 50)
    private String userName;

    @Column(name = "image_name",nullable = false)
    private String imageName;

    @Column(name = "image_full_path",unique = true,nullable = false)
    private String imageFullPath;

    @Column(name = "image_type",nullable = false)
    private String imageType;

}
