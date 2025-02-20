package com.example.Blogging.service.Login;

import com.example.Blogging.DAO.request.LoginRequest;
import com.example.Blogging.DAO.response.LoginResponse;
import com.example.Blogging.Utility.IpAddressUtils;
import com.example.Blogging.jwt.JwtProvider;
import com.example.Blogging.Utility.OtpUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final OtpUtility otpUtility;
    private final IpAddressUtils ipAddressUtils;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtProvider jwtProvider, OtpUtility otpService, IpAddressUtils ipAddressUtils){
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.otpUtility = otpService;
        this.ipAddressUtils = ipAddressUtils;
    }


    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {

        if(!ipAddressUtils.doesIpExist()){
            ipAddressUtils.registerIpAddressOfAUser();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );

        String jwt = jwtProvider.generateJwtToken(authentication);

        LoginResponse responseDTO = new LoginResponse();

        responseDTO.setDateTime(LocalDateTime.now());
        if(jwt != null) {

            otpUtility.createOtp(loginRequest.getEmail());

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
}
