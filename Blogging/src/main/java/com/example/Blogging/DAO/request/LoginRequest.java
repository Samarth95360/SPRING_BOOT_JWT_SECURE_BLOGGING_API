package com.example.Blogging.DAO.request;


import com.example.Blogging.CustomValidator.Password.ValidPassword;
import com.example.Blogging.DAO.response.LoginResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    @Email
    private String email;

    @ValidPassword
    @NotBlank
    @NotEmpty
    private String password;

}
