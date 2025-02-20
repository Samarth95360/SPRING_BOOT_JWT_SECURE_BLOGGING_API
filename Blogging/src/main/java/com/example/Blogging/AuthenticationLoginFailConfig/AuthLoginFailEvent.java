package com.example.Blogging.AuthenticationLoginFailConfig;

import com.example.Blogging.Models.User;
import com.example.Blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthLoginFailEvent implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserRepo userRepo;

    @Autowired
    public AuthLoginFailEvent(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        String userEmail = event.getAuthentication().getName();

//        System.out.println("Event -> "+userEmail);

        User user = userRepo.findByEmail(userEmail);

        if(user != null){

            if(user.getFailedLoginAttempt() > 3){
                user.setAccountLocked(true);
                user.setTimeStamp(LocalDateTime.now());
            }else {
                user.setFailedLoginAttempt(user.getFailedLoginAttempt()+1);
            }
            userRepo.save(user);
        }

    }

}
