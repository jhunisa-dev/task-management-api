package com.taskmanager.task_management_api.controller;

import com.taskmanager.task_management_api.dto.TaskRequest;
import com.taskmanager.task_management_api.dto.TaskResponse;
import com.taskmanager.task_management_api.entity.TaskStatus;
import com.taskmanager.task_management_api.repository.UserRepository;
import com.taskmanager.task_management_api.service.TaskService;
import com.taskmanager.task_management_api.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService,
                          UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskService.getTasks(getCurrentUserId(), status, pageable);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id, getCurrentUserId());
    }

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request) {
        return taskService.createTask(request, getCurrentUserId());
    }

    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isOwner(#id)")
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable("id") Long id, // <--- Add "id" here
            @Valid @RequestBody TaskRequest request) {
        return taskService.updateTask(id, request, getCurrentUserId());
    }

    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isOwner(#id)")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) { // <--- Add "id" here
        taskService.deleteTask(id, getCurrentUserId());
    }

    @GetMapping("/debug-me")
    public void debugUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
    }
}

