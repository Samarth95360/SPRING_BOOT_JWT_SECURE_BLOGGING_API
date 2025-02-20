package com.example.Blogging.service.Otp;

import com.example.Blogging.DAO.response.LoginResponse;
import com.example.Blogging.Models.User;
import com.example.Blogging.Utility.OtpUtility;
import com.example.Blogging.repo.UserRepo;
import com.example.Blogging.service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ResendOtpService {

    private final OtpUtility otpUtility;
    private final UserRepo userRepo;
    private final EmailService emailService;

    @Autowired
    public ResendOtpService(OtpUtility otpUtility, UserRepo userRepo, EmailService emailService) {
        this.otpUtility = otpUtility;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    public ResponseEntity<String> resendOtpToUser(UUID userId){

        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){
            String email = user.get().getEmail();
            if(email != null){
                otpUtility.createOtp(email);
                return new ResponseEntity<>("New Otp Send Success",HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Invalid Request Try Login Again",HttpStatus.BAD_REQUEST);

    }

}
