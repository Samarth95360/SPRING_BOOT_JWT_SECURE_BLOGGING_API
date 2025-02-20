package com.integration.imageStorage.dirCreation.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    private static final String JWT_HEADER = "Authorization";
    private static final String SECRET_KEY = "dfkdhfdnvlkndfne673453474gfbivlvkjoibhjjhXTYTUYIGUHOBTYFYIy7t68tibjkbu^*IYBGJVYGol";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {

        String jwt = request.getHeader(JWT_HEADER);

//        System.out.println("Jwt :- "+jwt);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);

            try {
                SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Check expiration
                if (claims.getExpiration().before(new Date())) {
                    throw new ExpiredJwtException(null, claims, "JWT Token is expired");
                }

                // Extract roles and other claims
                String username = claims.getSubject();
                String authority = claims.get("authorities", String.class);
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authority);

                // Set authentication in the SecurityContext
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException ex) {
                throw new InsufficientAuthenticationException("JWT Token is expired");
            } catch (JwtException | IllegalArgumentException ex) {
                throw new InsufficientAuthenticationException("Invalid JWT Token");
            }
        } else {
            throw new InsufficientAuthenticationException("Missing JWT Token");
        }

        filterChain.doFilter(request, response);
    }
}
