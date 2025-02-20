package com.example.Blogging.repo;

import com.example.Blogging.DTO.UserDTO;
import com.example.Blogging.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    User findByEmail(String email);

    @Query("select new com.example.Blogging.DTO.UserDTO(u.id,u.fullName,u.email,u.role,u.failedLoginAttempt,u.accountLocked,u.createdAt,u.timeStamp) from User u where u.id = :userId")
    Optional<UserDTO> findUserWithoutPost(@Param("userId") UUID userId);

    List<User> findAllByAccountLockedTrue();

}
