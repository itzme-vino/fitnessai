package com.app.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required to sign up")
    @Email(message = "Invalid email format")
    private String email;
    private String keycloakId;
    @NotBlank(message = "Password is required to sign up")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String firstName;
    private String lastName;
}
