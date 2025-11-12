package com.sdsu.backend.service;

import com.sdsu.backend.model.User;
// import com.sdsu.backend.model.UserSettings;
import com.sdsu.backend.repository.UserRepository;
// import com.sdsu.backend.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    // private final UserSettingsRepository userSettingsRepository;

    // UserSettingsRepository userSettingsRepository (REMOVED THIS PARAMETER FROM
    // USERSERVICE PUT BACK AFTER)
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // this.userSettingsRepository = userSettingsRepository;
    }

    public User createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        // UserSettings defaultSettings = new UserSettings(user, false, false);
        // user.setUserSettings(defaultSettings);

        return userRepository.save(user);
    }

    // Fetch All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update User Information
    public User updateUser(Long id, String email, String password, String name) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (email != null)
            user.setEmail(email);
        if (password != null)
            user.setPassword(password);
        if (name != null)
            user.setUsername(name);

        return userRepository.save(user);
    }

    // Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}