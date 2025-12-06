package com.sdsu.backend.dto;

public class LoginRequest {

    private String email;
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
