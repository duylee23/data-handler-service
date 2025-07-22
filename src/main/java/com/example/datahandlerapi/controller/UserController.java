package com.example.datahandlerapi.controller;

import com.example.datahandlerapi.dto.UserDTO;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.entity.User;
import com.example.datahandlerapi.repository.UserRepository;
import com.example.datahandlerapi.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping("/add")
    private ResponseEntity<ApiResponse<UserDTO>> addNewUser(@RequestBody UserDTO dto) {
        UserDTO createdUser = this.userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserDTO>builder()
                        .status("success")
                        .message("User created successfully")
                        .data(createdUser)
                        .build());
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listUsers() {
        List<UserDTO> list = this.userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                ApiResponse.<List<UserDTO>>builder()
                        .status("success")
                        .message("User list")
                        .data(list)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok(
          ApiResponse.<Void>builder()
                  .status("success")
                  .message("Delete user successfully!")
                  .build()
        );
    }
}
