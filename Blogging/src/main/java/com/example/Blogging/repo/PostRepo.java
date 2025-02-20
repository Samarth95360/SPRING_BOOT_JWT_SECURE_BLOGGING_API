package com.example.Blogging.repo;

import com.example.Blogging.Models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<Posts , UUID> {

    @Query("select p from Posts p where p.user.id = :userId order by p.timeStamp desc")
    List<Posts> findAllPostByUserId(@Param("userId") UUID userId);

    @Query("select p from Posts p where p.approved = true order by p.timeStamp desc")
    List<Posts> findAllApprovedPosts();

    @Query("select p from Posts p order by p.timeStamp desc")
    List<Posts> findAllPostDesc();

}
