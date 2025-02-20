package com.example.Blogging.AuthenticationLoginFailConfig;

import com.example.Blogging.Models.User;
import com.example.Blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UnlockAccountService {

    private static final int LOCK_TIME_DURATION = 3;

    private final UserRepo userRepo;

    @Autowired
    public UnlockAccountService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void unlockAccountService(){
        List<User> userList = userRepo.findAllByAccountLockedTrue();

        userList.forEach(
                user -> {
                    if(user.getTimeStamp().plusMinutes(LOCK_TIME_DURATION).isBefore(LocalDateTime.now())){
                        user.setAccountLocked(false);
                        user.setTimeStamp(null);
                        user.setFailedLoginAttempt(0);
                        userRepo.save(user);
                    }
                }
        );
    }

}
