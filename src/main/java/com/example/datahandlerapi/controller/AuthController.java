package com.example.datahandlerapi.controller;

import com.example.datahandlerapi.dto.request.LoginRequest;
import com.example.datahandlerapi.dto.response.AuthResponse;
import com.example.datahandlerapi.service.AuthService;
import com.example.datahandlerapi.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Setter
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails user = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(user);
            String role = user.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("USER");
            role = role.startsWith("ROLE_") ? role.substring(5) : role;

            System.out.println("✅ LOGIN SUCCESS: " + user.getUsername() + "✅ ROLE " + role);

            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), role));
        } catch (BadCredentialsException e) {
            System.out.println("❌ LOGIN FAILED: bad credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println("❌ LOGIN ERROR: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
