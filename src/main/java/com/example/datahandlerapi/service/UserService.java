package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO dto);
    List<UserDTO> getAllUsers();
    void deleteUser(Long id);
}
