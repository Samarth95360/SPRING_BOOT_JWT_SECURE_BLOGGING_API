package com.example.Blogging.DAO.request;


import com.example.Blogging.CustomValidator.Password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPasswordRequest {

    @NotBlank
    @NotEmpty
    @ValidPassword
    private String password;

    @NotBlank
    @NotEmpty
    @ValidPassword
    private String confirmationPassword;
}
