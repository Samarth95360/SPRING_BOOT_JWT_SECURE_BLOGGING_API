package com.integration.imageStorage.dirCreation.image.repo;

import com.integration.imageStorage.dirCreation.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ImageRepo extends JpaRepository<Image, UUID> {

    @Query("select i from Image i where i.imageFullPath= :fullPath")
    Image findByImageFullPath(String fullPath);
}
