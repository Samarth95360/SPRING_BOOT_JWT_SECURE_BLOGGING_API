package com.example.Blogging.DAO.response;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileResponse {

    private UUID id;

    @NotBlank(message = "Bio can't be blank")
    private String bio;

    private LocalDate birthDate;

    private String address;

    private String image = null;
}
