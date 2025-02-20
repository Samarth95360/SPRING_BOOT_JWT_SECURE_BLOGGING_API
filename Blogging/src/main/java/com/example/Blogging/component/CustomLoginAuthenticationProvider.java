package com.example.Blogging.component;

import com.example.Blogging.Utility.IpAddressUtils;
import com.example.Blogging.service.Email.EmailService;
import com.example.Blogging.service.Security.UserService;
import com.example.Blogging.service.Security.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginAuthenticationProvider implements AuthenticationProvider {

    private final UserServiceImpl userServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApplicationEventPublisher publisher;
    private final IpAddressUtils ipAddressUtils;
    private final EmailService emailService;

    @Autowired
    public CustomLoginAuthenticationProvider(UserServiceImpl userServiceImpl, BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationEventPublisher publisher, IpAddressUtils ipAddressUtils, EmailService emailService){
        this.userServiceImpl = userServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.publisher = publisher;
        this.ipAddressUtils = ipAddressUtils;
        this.emailService = emailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(ipAddressUtils.isAccountBLocked()){
            emailService.senMail(email,"Ip BLocked","Ip Address of this User Pc is blocked Try after some time");
            throw new AccountExpiredException("Ip Address of this Account is blocked Try after some time");
        }

        UserService userService = (UserService) userServiceImpl.loadUserByUsername(email);

        if(!userService.isAccountNonLocked()){
            emailService.senMail(email,"Account Locked","Account Locked because of multiple failed login attempt");
            throw new AccountExpiredException("Account Locked because of multiple failed login attempt");
        }

        if(!bCryptPasswordEncoder.matches(password, userService.getPassword())){
            ipAddressUtils.blockIpAddress();
            publisher.publishEvent(new AuthenticationFailureBadCredentialsEvent(authentication,new BadCredentialsException("Invalid Password")));
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userService,null,userService.getOtpAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
