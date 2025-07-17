package com.example.datahandlerapi.controller;

import com.example.datahandlerapi.dto.UserDTO;
import com.example.datahandlerapi.entity.User;
import com.example.datahandlerapi.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @PostMapping("/add")
    private ResponseEntity<?> addNewUser(@RequestBody UserDTO dto) {
        if(this.userRepository.existsByUsername(dto.getUsername())){
            return ResponseEntity.badRequest().body("Username already exist!");
        }
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .role(dto.getRole().toUpperCase())
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully!");
    }

    @GetMapping("/list")
    public ResponseEntity<?> listUsers() {
        try {
            List<User> userList = userRepository.findAll();

            if (userList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        Map.of(
                                "status", "success",
                                "message", "No users found",
                                "data", Collections.emptyList()
                        )
                );
            }
            return ResponseEntity.ok(
                    Map.of(
                            "status", "success",
                            "count", userList.size(),
                            "data", userList.stream()
                                    .map(user -> Map.of(
                                            "id", user.getId(),
                                            "username", user.getUsername(),
                                            "role", user.getRole(),
                                            "email", user.getEmail()
                                    ))
                                    .toList()
                    )
            );

        } catch (Exception ex) {
            log.error("Failed to retrieve users", ex);
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", "error",
                            "message", "Failed to fetch users",
                            "error", ex.getMessage()
                    )
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
