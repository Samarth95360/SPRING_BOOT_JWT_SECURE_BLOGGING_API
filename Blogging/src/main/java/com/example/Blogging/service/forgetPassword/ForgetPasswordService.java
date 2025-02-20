package com.example.Blogging.service.forgetPassword;

import com.example.Blogging.DAO.request.NewPasswordRequest;
import com.example.Blogging.DAO.response.LoginResponse;
import com.example.Blogging.DTO.TokenDTO;
import com.example.Blogging.Models.User;
import com.example.Blogging.Utility.TokenUtility;
import com.example.Blogging.jwt.JwtProvider;
import com.example.Blogging.repo.UserRepo;
import com.example.Blogging.service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgetPasswordService {

    private final EmailService emailService;
    private final UserRepo userRepo;
    private final TokenUtility tokenUtility;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ForgetPasswordService(EmailService emailService, UserRepo userRepo, TokenUtility tokenUtility, JwtProvider jwtProvider, BCryptPasswordEncoder passwordEncoder){
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.tokenUtility = tokenUtility;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> forgetPassword(String userEmail){
        User user = userRepo.findByEmail(userEmail);
        if(user != null){
            if(user.getEmail().equals(userEmail)){
                tokenUtility.createToken(userEmail);
            }
        }
        return new ResponseEntity<>("If the email exists, you will receive a verification token in your provided email.", HttpStatus.OK);
    }

    public ResponseEntity<LoginResponse> tokenVerification(String token){

        TokenDTO response = tokenUtility.tokenVerification(token);

        if(response == null){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwt(null);
            loginResponse.setMessage("Invalid Token Please try again");
            loginResponse.setDateTime(LocalDateTime.now());
            loginResponse.setJwtTokenAllocated(false);
            return new ResponseEntity<>(loginResponse,HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(response.getUserEmail(),"", Collections.singletonList(new SimpleGrantedAuthority("ROLE_TOKEN")));

        String jwt = jwtProvider.generateJwtToken(authentication);

        LoginResponse responseDTO = new LoginResponse();

        responseDTO.setDateTime(LocalDateTime.now());
        if(jwt != null) {
            responseDTO.setJwt(jwt);
            responseDTO.setMessage("Jwt created Success");
            responseDTO.setJwtTokenAllocated(true);
        }else{
            responseDTO.setJwt(null);
            responseDTO.setMessage("Jwt Creation Fail");
            responseDTO.setJwtTokenAllocated(false);
        }
        return jwt == null ? new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }

    public ResponseEntity<String> validatePasswordAndChange(NewPasswordRequest request , UUID userId){
        if(!request.getPassword().equals(request.getConfirmationPassword())){
            return new ResponseEntity<>("Invalid Password . Please provide a valid password in both the fields",HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()){
            user.get().setPassword(passwordEncoder.encode(request.getPassword()));
            userRepo.save(user.get());
            return new ResponseEntity<>("Password Change Success" , HttpStatus.OK);
        }
        return new ResponseEntity<>("SomeThing went wrong . Try Again" , HttpStatus.BAD_GATEWAY);
    }

}
