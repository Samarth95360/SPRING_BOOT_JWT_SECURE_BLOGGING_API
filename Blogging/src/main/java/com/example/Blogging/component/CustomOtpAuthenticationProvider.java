package com.example.Blogging.component;

import com.example.Blogging.Models.User;
import com.example.Blogging.Utility.OtpUtility;
import com.example.Blogging.repo.UserRepo;
import com.example.Blogging.service.Security.UserService;
import com.example.Blogging.service.Security.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomOtpAuthenticationProvider implements AuthenticationProvider {

    private final UserRepo userRepo;
    private final OtpUtility otpUtility;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public CustomOtpAuthenticationProvider(UserRepo userRepo, OtpUtility otpUtility, UserServiceImpl userServiceImpl) {
        this.userRepo = userRepo;
        this.otpUtility = otpUtility;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UUID userId = UUID.fromString(authentication.getName());
        String otp = authentication.getCredentials().toString();

        Optional<User> user = userRepo.findById(userId);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("Invalid User");
        }

        String email = user.get().getEmail();

        UserService userService = (UserService) userServiceImpl.loadUserByUsername(email);

        if(!otpUtility.validateOtp(otp,email)){
            throw new BadCredentialsException("invalid Opt");
        }

        return new OtpAuthenticationToken(userService,null,userService.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
