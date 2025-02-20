package com.example.Blogging.Utility;

import com.example.Blogging.service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OtpUtility {

    private final EmailService emailService;

    private final Map<String,String> otp = new HashMap<>();

    @Autowired
    public OtpUtility(EmailService emailService) {
        this.emailService = emailService;
    }

    public void createOtp(String userEmail){
        String createdOtp = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);
        if(otp.containsKey(userEmail)){
            otp.remove(userEmail);
        }
        otp.put(userEmail,createdOtp);
        System.out.println("Otp for email "+userEmail+" is :- "+createdOtp);
        emailService.senMail(userEmail,"Login Otp","Otp for Login is :- "+createdOtp);
    }

    public boolean validateOtp(String toVerifyOtp,String userEmail){

        String value = otp.get(userEmail);

        if(value != null || value.equals(toVerifyOtp)){
            otp.remove(userEmail);
            return true;
        }
        return false;

    }

}
