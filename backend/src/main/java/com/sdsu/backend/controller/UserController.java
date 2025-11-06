package com.sdsu.backend.controller;

import com.sdsu.backend.dto.CreateUserRequest;
import com.sdsu.backend.dto.UpdateEventRequest;
import com.sdsu.backend.dto.UpdateUserRequest;
import com.sdsu.backend.model.User;
import com.sdsu.backend.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // ===== CREATE USER =====
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        try {

            User user = new User();

            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setUsername(request.getName());
            user.setActiveStatusTrue();

            User saved = userService.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== READ ALL =====
    @GetMapping
    public ResponseEntity<List<User>> getAllActiveUsers(@RequestParam boolean activeStatus) {
        try {
            List<User> users = userService.getUserByActiveStatus(activeStatus);

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== READ ONE =====

    // Read Through ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Read Through Email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Read Through Password

    @GetMapping("/password/{password}")
    public ResponseEntity<User> getUserByPassword(@PathVariable String password) {
        Optional<User> user = userService.getUserByPassword(password);

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Read Through Name
    @GetMapping("/name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        Optional<User> user = userService.getUserByName(name);

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== UPDATE =====

    // Update a User via ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody UpdateUserRequest request) {
        try {
            Optional<User> userOpt = userService.findById(id);

            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();

            // This is all based on the calandarEventController.java

            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null) {
                user.setPassword(request.getPassword());
            }
            if (request.getName() != null) {
                user.setUsername(request.getName());
            }

            User updatedUser = userService.save(user);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Update a User via Email
    @PutMapping("/email/{email}")
    public ResponseEntity<User> updateUserByEmail(@PathVariable String email,
                                                  @RequestBody UpdateUserRequest request) {
        try {
            Optional<User> userOpt = userService.getUserByEmail(email);

            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();

            // This is all based on the calandarEventController.java

            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null) {
                user.setPassword(request.getPassword());
            }
            if (request.getName() != null) {
                user.setUsername(request.getName());
            }

            User updatedUser = userService.save(user);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== DELETE USER ======
    // Delete User via ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    //Delete User via Email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
