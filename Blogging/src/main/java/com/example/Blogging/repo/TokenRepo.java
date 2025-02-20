package com.example.Blogging.repo;

import com.example.Blogging.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TokenRepo extends JpaRepository<Token, UUID> {

    Token findByToken(String token);

    Token findByUserEmail(String userEmail);
}
