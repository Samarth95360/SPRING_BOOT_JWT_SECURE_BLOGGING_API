package com.example.Blogging.service.Otp;

import com.example.Blogging.DAO.response.LoginResponse;
import com.example.Blogging.Models.User;
import com.example.Blogging.OtpLoginSuccessEvent.LoginSuccessEvent;
import com.example.Blogging.Utility.IpAddressUtils;
import com.example.Blogging.Utility.OtpUtility;
import com.example.Blogging.component.OtpAuthenticationToken;
import com.example.Blogging.jwt.JwtProvider;
import com.example.Blogging.repo.UserRepo;
import com.example.Blogging.service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OtpService {

    private final OtpUtility otpUtility;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher publisher;
    private final IpAddressUtils ipAddressUtils;
    private final EmailService emailService;
    private final UserRepo userRepo;

    @Autowired
    public OtpService(OtpUtility otpUtility, AuthenticationManager authenticationManager, JwtProvider jwtProvider, ApplicationEventPublisher publisher, IpAddressUtils ipAddressUtils, EmailService emailService, UserRepo userRepo) {
        this.otpUtility = otpUtility;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.publisher = publisher;
        this.ipAddressUtils = ipAddressUtils;
        this.emailService = emailService;
        this.userRepo = userRepo;
    }

    public ResponseEntity<LoginResponse> verifyUserOtp(UUID userId, String otp){
        Authentication authentication = authenticationManager.authenticate(
                new OtpAuthenticationToken(userId,otp)
        );

        String jwt = jwtProvider.generateJwtToken(authentication);

        //For mail service fetching userEmail

        Optional<User> user = userRepo.findById(userId);
        String userEmail = user.get().getEmail();

        LoginResponse responseDTO = new LoginResponse();

        responseDTO.setDateTime(LocalDateTime.now());
        if(jwt != null) {
            responseDTO.setJwt(jwt);
            responseDTO.setMessage("Jwt created Success");
            responseDTO.setJwtTokenAllocated(true);
            publisher.publishEvent(new LoginSuccessEvent(this,userId, ipAddressUtils.getIpAddress()));
            emailService.senMail(userEmail,"Login Success","Login Success");
        }else{
            responseDTO.setJwt(null);
            responseDTO.setMessage("Jwt Creation Fail");
            responseDTO.setJwtTokenAllocated(false);
            emailService.senMail(userEmail,"Login Failed","Invalid Otp . Please Login Again");
        }
        return jwt == null ? new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(responseDTO,HttpStatus.CREATED);

    }

}
