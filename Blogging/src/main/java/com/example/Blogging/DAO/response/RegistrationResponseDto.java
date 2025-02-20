package com.example.Blogging.DAO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationResponseDto {

    private String message;
    private HttpStatus statusCode;
    private boolean isRegistered;
    private LocalDateTime dateTime;

}
