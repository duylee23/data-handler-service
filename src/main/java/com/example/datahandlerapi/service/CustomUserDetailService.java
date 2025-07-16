package com.example.datahandlerapi.service;

import com.example.datahandlerapi.entity.User;
import com.example.datahandlerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //get user from db
        User userDb = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(userDb.getUsername())
                .password(userDb.getPassword())
                .roles(userDb.getRole()) // Spring Security sẽ thêm tiền tố "ROLE_"
                .build();
    }
}
