package com.taskmanager.task_management_api.controller;

import com.taskmanager.task_management_api.dto.*;
import com.taskmanager.task_management_api.entity.User;
import com.taskmanager.task_management_api.repository.UserRepository;
import com.taskmanager.task_management_api.security.JwtUtil;
import com.taskmanager.task_management_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);
        return ResponseEntity.ok(
                new ApiResponse(true, "User registered successfully")
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token);
    }
}
