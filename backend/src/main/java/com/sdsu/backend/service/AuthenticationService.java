package com.sdsu.backend.service;

import com.sdsu.backend.dto.LoginResponse;
import com.sdsu.backend.model.User;

import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {

    LoginResponse authenticateAndGetToken(String email, String password) throws Exception;

    User getAuthenticatedUser(String token);
}
