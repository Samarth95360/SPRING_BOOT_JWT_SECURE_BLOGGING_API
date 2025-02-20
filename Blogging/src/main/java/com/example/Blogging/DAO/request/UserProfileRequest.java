package com.example.Blogging.DAO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileRequest {
    @NotBlank(message = "Bio can't be blank")
    private String bio;

    private LocalDate birthDate;

    private String address;
}
