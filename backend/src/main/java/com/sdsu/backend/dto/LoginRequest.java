package com.sdsu.backend.dto;

//for validation (@Email, @NotBlank, etc...)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class LoginRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Getter @Setter
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Getter @Setter
    private String password;

    public LoginRequest() {
        //Default Constructor Required By JPA
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}