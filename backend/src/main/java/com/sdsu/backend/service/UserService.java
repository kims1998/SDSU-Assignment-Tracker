package com.sdsu.backend.service;

import com.sdsu.backend.model.User;
// import com.sdsu.backend.model.UserSettings;
import com.sdsu.backend.repository.UserRepository;
// import com.sdsu.backend.repository.UserSettingsRepository;
import jakarta.transaction.Transactional;
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

    /*
    public User createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        // UserSettings defaultSettings = new UserSettings(user, false, false);
        // user.setUserSettings(defaultSettings);

        return userRepository.save(user);
    }
     */

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException(("User cannot be null"));
        }
        if (user.getEmail() == null) {
            throw new IllegalArgumentException(("User must have an Email"));
        }
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException(("User must have both a Username and/or Password"));
        }

        return userRepository.save(user);
    }

    /*
    // Fetch All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

     */

    // Find User by ID
    public Optional<User> findById (Long id) { return userRepository.findById(id); }

    // Find User by Email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    // Find User by Password
    public Optional<User> getUserByPassword(String password) {
        return userRepository.findUserByPassword(password);
    }

    // Find User by Name
    public Optional<User> getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    // Find All Active Users
    public List<User> getUserByActiveStatus(boolean activeStatus) {
        return userRepository.findUserByActiveStatus(activeStatus);
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

    @Transactional
    public void deleteUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null to be delete");
        }
        userRepository.deleteByEmail(email);
    }
}