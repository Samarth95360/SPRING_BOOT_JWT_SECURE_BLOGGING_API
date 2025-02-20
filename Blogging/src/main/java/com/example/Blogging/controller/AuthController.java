package com.example.Blogging.controller;

import com.example.Blogging.DAO.request.LoginRequest;
import com.example.Blogging.DAO.request.NewPasswordRequest;
import com.example.Blogging.DAO.request.RegisterRequest;
import com.example.Blogging.DAO.response.LoginResponse;
import com.example.Blogging.DAO.response.RegistrationResponseDto;
import com.example.Blogging.service.Login.LoginService;
import com.example.Blogging.service.Otp.OtpService;
import com.example.Blogging.service.Otp.ResendOtpService;
import com.example.Blogging.service.Registration.RegistrationService;
import com.example.Blogging.service.forgetPassword.ForgetPasswordService;
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
    private final ResendOtpService resendOtpService;
    private final ForgetPasswordService forgetPasswordService;

    @Autowired
    public AuthController(RegistrationService registrationService, LoginService loginService, OtpService otpService, ResendOtpService resendOtpService, ForgetPasswordService forgetPasswordService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
        this.otpService = otpService;
        this.resendOtpService = resendOtpService;
        this.forgetPasswordService = forgetPasswordService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return registrationService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return loginService.loginUser(loginRequest);
    }

    @GetMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@AuthenticationPrincipal UUID userId){
        return resendOtpService.resendOtpToUser(userId);
    }

    @PostMapping("/otp")
    public ResponseEntity<LoginResponse> verifyOtpOfAUser(@AuthenticationPrincipal UUID userId, @RequestParam String otp){
        return otpService.verifyUserOtp(userId , otp);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> userForgetPassword(@RequestParam String userEmail){
        return forgetPasswordService.forgetPassword(userEmail);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<LoginResponse> verifyToken(@RequestParam String token){
        return forgetPasswordService.tokenVerification(token);
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> changePassword(@RequestBody NewPasswordRequest passwordRequest,@AuthenticationPrincipal UUID userId){
        return forgetPasswordService.validatePasswordAndChange(passwordRequest, userId);
    }

}
