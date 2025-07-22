package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.UserDTO;
import com.example.datahandlerapi.entity.User;
import com.example.datahandlerapi.mapper.UserMapper;
import com.example.datahandlerapi.repository.UserRepository;
import com.example.datahandlerapi.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImplement implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    @Override
    public UserDTO createUser(UserDTO dto) {
        if(this.userRepository.existsByUsername(dto.getUsername())){
            throw new IllegalArgumentException("Username already exist!");
        }
        User user = this.userMapper.toEntity(dto, passwordEncoder);
        return this.userMapper.toDTO(this.userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("Retrieved {} users from database", users.size());
        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        if(!this.userRepository.existsById(id)){
            throw new NoSuchElementException("user not found");
        }
        this.userRepository.deleteById(id);
    }
}