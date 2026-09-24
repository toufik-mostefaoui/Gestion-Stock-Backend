package com.example.gestionstock.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class loginDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
