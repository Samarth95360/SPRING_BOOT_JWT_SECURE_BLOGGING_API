package com.example.Blogging.controller;

import com.example.Blogging.DAO.request.LoginRequest;
import com.example.Blogging.DAO.request.RegisterRequest;
import com.example.Blogging.DAO.response.LoginResponse;
import com.example.Blogging.DAO.response.RegistrationResponseDto;
import com.example.Blogging.service.Login.LoginService;
import com.example.Blogging.service.Otp.OtpService;
import com.example.Blogging.service.Registration.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final OtpService otpService;

    @Autowired
    public AuthController(RegistrationService registrationService, LoginService loginService, OtpService otpService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return registrationService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return loginService.loginUser(loginRequest);
    }

    @PostMapping("/otp")
    public ResponseEntity<LoginResponse> verifyOtpOfAUser(@AuthenticationPrincipal UUID userId, @RequestParam String otp){
        return otpService.verifyUserOtp(userId , otp);
    }

}
