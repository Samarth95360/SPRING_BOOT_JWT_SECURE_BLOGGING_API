package com.example.Blogging.service.Registration;

import com.example.Blogging.DAO.request.RegisterRequest;
import com.example.Blogging.DAO.response.RegistrationResponseDto;
import com.example.Blogging.Models.Role;
import com.example.Blogging.Models.User;
import com.example.Blogging.repo.UserRepo;
import com.example.Blogging.service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RegistrationService(UserRepo userRepo, EmailService emailService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<RegistrationResponseDto> registerUser(RegisterRequest registerRequest) {

        User user = userRepo.findByEmail(registerRequest.getEmail());

        if(user != null){
            RegistrationResponseDto responseDto = new RegistrationResponseDto();
            responseDto.setRegistered(false);
            responseDto.setMessage("User already registered with this Email");
            responseDto.setDateTime(LocalDateTime.now());
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST);
            emailService.senMail(registerRequest.getEmail(), "Regarding Failed Registration" ,"We are sorry to inform you that this email is already benn taken try from another email . Thank You");
            return new ResponseEntity<>(responseDto,HttpStatus.NOT_ACCEPTABLE);
        }

        User user1 = new User();
        user1.setFullName(registerRequest.getFullName());
        user1.setEmail(registerRequest.getEmail());
        user1.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user1.setRole(Role.valueOf(registerRequest.getRole()));

        userRepo.save(user1);


        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        responseDto.setRegistered(true);
        responseDto.setMessage("User Created Successfully");
        responseDto.setDateTime(LocalDateTime.now());
        responseDto.setStatusCode(HttpStatus.CREATED);
        emailService.senMail(registerRequest.getEmail(), "Success Registration" ,"You Have successfully Registered");

        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);

    }


}
