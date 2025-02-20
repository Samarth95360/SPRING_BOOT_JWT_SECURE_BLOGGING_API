package com.example.Blogging.jwt;

import com.example.Blogging.Models.User;
import com.example.Blogging.Utility.EncryptionUtils;
import com.example.Blogging.Utility.RandomAlphaNumericUUID;
import com.example.Blogging.repo.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Component
public class JwtProvider {

    private final UserRepo userRepo;

    private final SecretKey key = Keys.hmacShaKeyFor(JwtConst.SECURITY_KEY.getBytes());

    @Autowired
    public JwtProvider(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public String generateJwtToken(Authentication authentication){
        String userEmail = authentication.getName();
        User user = userRepo.findByEmail(userEmail);

        if (user == null){
            throw new  IllegalArgumentException("user not found");
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String role = populateRoles(authorities);

        Instant expirationTime = role.equals("ROLE_OTP") || role.equals("ROLE_TOKEN")
                ? Instant.now().plusSeconds(300) // 5 minutes
                : Instant.now().plusSeconds(86400); // 24 hours


        // Handle encryption of userId
        String encryptedUserId = null;
        try {
            String saltedUserId = RandomAlphaNumericUUID.addSalt(user.getId().toString());

            encryptedUserId = EncryptionUtils.encrypt(saltedUserId);

        } catch (Exception ex) {
            System.err.println("Failed to encrypt userId: " + ex.getMessage());
            throw new RuntimeException("Error while encrypting userId", ex);
        }

        return Jwts.builder()
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expirationTime))
                .claim("userId",encryptedUserId)
                .claim("authorities",role)
                .signWith(key)
                .compact();
    }

    private String populateRoles(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = new HashSet<>();
        for (GrantedAuthority authority : authorities){
            roles.add(authority.getAuthority());
        }
        return String.join(",",roles);
    }

}
