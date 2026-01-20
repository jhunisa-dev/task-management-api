package com.taskmanager.task_management_api.controller;

import com.taskmanager.task_management_api.dto.RegisterRequest;
import com.taskmanager.task_management_api.entity.Role;
import com.taskmanager.task_management_api.entity.User;
import com.taskmanager.task_management_api.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void createAdmin(@RequestBody RegisterRequest request) {
        User admin = new User();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }
}
