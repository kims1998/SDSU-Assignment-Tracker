package com.sdsu.backend.service;

import com.sdsu.backend.dto.LoginResponse;
import com.sdsu.backend.model.User;
import com.sdsu.backend.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtTokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse authenticateAndGetToken(String email, String password) throws Exception {

        // 1. Find the user by email
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        User user = userOpt.get();

        // 2. Check the raw password against the HASHED password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        // 3. Credentials are valid: Generate the JWT Token
        String jwtToken = tokenService.generateToken(user.getEmail(), user.getId());

        // 4. Return successful response
        return new LoginResponse(
                jwtToken,
                user.getId(),
                user.getEmail()
        );
    }
    @Override
    public User getAuthenticatedUser(String token) {

        // 1. Validate the token and extract the User ID
        // The token must be non-null and valid
        if (token != null && tokenService.validateToken(token)) {

            // 2. extract the user's ID from the token claims
            Long userId = tokenService.getUserIdFromToken(token);

            // 3. Look up the user in the database
            Optional<User> userOpt = userRepository.findById(userId);

            // 4/ Return the user object if found, else return null
            return userOpt.orElse(null);
        }

        // If the token is null, or expired
        return null;
    }

}
