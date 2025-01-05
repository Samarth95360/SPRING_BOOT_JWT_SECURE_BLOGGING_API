package com.example.Blogging.DAO.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {

    private String fullName;

    @Email
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private String role;

}
