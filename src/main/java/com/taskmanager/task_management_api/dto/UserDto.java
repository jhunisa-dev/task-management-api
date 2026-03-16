package com.taskmanager.task_management_api.dto;

public record UserDto(
        Long id,
        String username,
        String email,
        String role
) {}
