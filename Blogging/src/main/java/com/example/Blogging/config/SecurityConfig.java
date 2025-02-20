package com.example.Blogging.config;

import com.example.Blogging.ExceptionHandler.AuthExceptionHandler;
import com.example.Blogging.component.CustomLoginAuthenticationProvider;
import com.example.Blogging.component.CustomOtpAuthenticationProvider;
import com.example.Blogging.jwt.JwtTokenValidator;
import com.example.Blogging.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserRepo userRepo;

    private final CustomLoginAuthenticationProvider loginAuthenticationProvider;
    private final CustomOtpAuthenticationProvider otpAuthenticationProvider;

    @Autowired
    public SecurityConfig(UserRepo userRepo, CustomLoginAuthenticationProvider loginAuthenticationProvider, CustomOtpAuthenticationProvider otpAuthenticationProvider){
        this.userRepo = userRepo;
        this.loginAuthenticationProvider = loginAuthenticationProvider;
        this.otpAuthenticationProvider = otpAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http , AuthExceptionHandler entryPoint) throws Exception{

        http
                .securityContext(
                        securityContext -> securityContext.requireExplicitSave(false)
                )
                .sessionManagement(
                        management -> management.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                ).authorizeHttpRequests(
                        Authorize -> Authorize
                                .requestMatchers("/auth/login","/auth/register","/auth/forget-password","/auth/verify-token").permitAll()
                                .requestMatchers("/api/user/**","/api/comment/**","/api/post/**","/api/user-profile","/api/profile-image").hasRole("USER")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/auth/otp","/auth/resend-otp").hasRole("OTP")
                                .requestMatchers("/auth/new-password").hasRole("TOKEN")
                                .anyRequest().permitAll()
                )
//                .authenticationProvider(loginAuthenticationProvider)
                .addFilterBefore(new JwtTokenValidator(userRepo) , BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling.authenticationEntryPoint(entryPoint)
                );
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        };

    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
//        return configuration.getAuthenticationManager();
//    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(loginAuthenticationProvider, otpAuthenticationProvider));
    }

}
