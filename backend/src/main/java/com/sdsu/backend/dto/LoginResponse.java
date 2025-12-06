package com.sdsu.backend.dto;

import com.sdsu.backend.model.User;

public class LoginResponse {

    private String token;
    private Long userId;
    private String email;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    // Getters
    public String getToken() {
        return token;
    }
    public Long getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }

    // Setters

    public void setToken() {
        this.token = token;
    }
    public void setUserId() {
        this.userId = userId;
    }
    public void setEmail() {
        this.email = email;
    }
}
