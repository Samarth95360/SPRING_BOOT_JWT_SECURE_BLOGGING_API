package com.example.Blogging.OtpLoginSuccessEvent;

import com.example.Blogging.Models.User;
import com.example.Blogging.Models.UserIpAddress;
import com.example.Blogging.repo.UserIpAddressRepo;
import com.example.Blogging.repo.UserRepo;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class LoginSuccessEventListener {

    private final UserRepo userRepo;
    private final UserIpAddressRepo userIpAddressRepo;

    public LoginSuccessEventListener(UserRepo userRepo, UserIpAddressRepo userIpAddressRepo) {
        this.userRepo = userRepo;
        this.userIpAddressRepo = userIpAddressRepo;
    }

    @EventListener
    public void loginSuccessEventListener(LoginSuccessEvent event){
        UUID userId = event.getUserId();
        String ipAddress = event.getIpAddress();

        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){
            user.get().setFailedLoginAttempt(0);
            user.get().setTimeStamp(null);
            user.get().setAccountLocked(false);
            userRepo.save(user.get());
        }

        UserIpAddress userIpAddress = userIpAddressRepo.findByIpAddress(ipAddress);
        if(userIpAddress!=null){
            userIpAddress.setAccountLockedTimeStamp(null);
            userIpAddress.setAccountLocked(false);
            userIpAddress.setFailedLoginAttempt(0);
            userIpAddressRepo.save(userIpAddress);
        }

    }

}
