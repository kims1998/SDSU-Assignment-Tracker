package com.sdsu.backend.dto;

import com.sdsu.backend.model.User;
import lombok.Getter;
import lombok.Setter;

public class LoginResponse {

    @Getter @Setter
    private String token;

    @Getter @Setter
    private Long userId;

    @Getter @Setter
    private String email;

    public LoginResponse() {
        //Default Constructor Required by JPA
    }

    public LoginResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }
}
