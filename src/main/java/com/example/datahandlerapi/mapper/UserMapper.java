package com.example.datahandlerapi.mapper;

import com.example.datahandlerapi.dto.UserDTO;
import com.example.datahandlerapi.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public User toEntity(UserDTO dto, PasswordEncoder encoder){
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .role(dto.getRole().toUpperCase())
                .build();
    }
}
