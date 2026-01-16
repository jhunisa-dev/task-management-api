package com.taskmanager.task_management_api.service;

import com.taskmanager.task_management_api.dto.AuthResponse;
import com.taskmanager.task_management_api.dto.LoginRequest;
import com.taskmanager.task_management_api.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}