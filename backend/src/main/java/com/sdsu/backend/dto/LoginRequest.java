package com.sdsu.backend.dto;

//for validation (@Email, @NotBlank, etc...)
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }



    // Setters
    public void setEmail() {
        this.email = email;
    }

    public void setPassword() {
        this.password = password;
    }


}
