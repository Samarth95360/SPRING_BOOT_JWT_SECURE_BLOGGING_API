package com.example.Blogging.jwt;

import com.example.Blogging.DTO.UserDTO;
import com.example.Blogging.Utility.EncryptionUtils;
import com.example.Blogging.Utility.RandomAlphaNumericUUID;
import com.example.Blogging.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JwtTokenValidator extends OncePerRequestFilter {

    private final UserRepo userRepo;

    @Autowired
    public JwtTokenValidator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if(path.equals("/auth/login") || path.equals("/auth/register") || path.equals("/auth/forget-password") || path.equals("/auth/verify-token")){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt = request.getHeader(JwtConst.JWT_HEADER);

        if(jwt != null && jwt.startsWith("Bearer ")){

            jwt = jwt.substring(7);

            try {

                SecretKey key = Keys.hmacShaKeyFor(JwtConst.SECURITY_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Check expiration
                if (claims.getExpiration().before(new Date())) {
                    throw new ExpiredJwtException(null, claims, "JWT Token is expired");
                }

                String encryptedUserId = String.valueOf(claims.get("userId"));
//                System.out.println("Encrypt user id :- "+encryptedUserId);
                UUID userId = null;
                try {
                    String decryptUserId = EncryptionUtils.decrypt(encryptedUserId);
//                    System.out.println("Decrypt user id :- "+decryptUserId);
                    userId = RandomAlphaNumericUUID.removeData(decryptUserId);
                }catch (Exception ex){
                    throw new RuntimeException("Error while Decrypting UserId");
                }

//                System.out.println("User id :- "+userId);

//                Optional<User> user = userRepo.findById(userId);

                Optional<UserDTO> user = userRepo.findUserWithoutPost(userId);

                if(user.isEmpty()){
                    throw new UsernameNotFoundException("Invalid UserId");
                }

                String authority = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authority);

                Authentication authentication = new UsernamePasswordAuthenticationToken(user.get().getId(),jwt,authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (ExpiredJwtException ex) {
                throw new InsufficientAuthenticationException("JWT Token is expired");
            } catch (JwtException | IllegalArgumentException ex) {
                throw new InsufficientAuthenticationException("Invalid JWT Token");
            }

        }else{
            throw new InsufficientAuthenticationException("Missing Jwt Token");
        }

        filterChain.doFilter(request,response);

    }
}
