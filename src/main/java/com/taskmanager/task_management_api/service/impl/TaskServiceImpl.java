package com.taskmanager.task_management_api.service.impl;

import com.taskmanager.task_management_api.dto.TaskRequest;
import com.taskmanager.task_management_api.dto.TaskResponse;
import com.taskmanager.task_management_api.entity.Task;
import com.taskmanager.task_management_api.entity.TaskStatus;
import com.taskmanager.task_management_api.entity.User;
import com.taskmanager.task_management_api.exception.AccessDeniedException;
import com.taskmanager.task_management_api.exception.ResourceNotFoundException;
import com.taskmanager.task_management_api.repository.TaskRepository;
import com.taskmanager.task_management_api.repository.UserRepository;
import com.taskmanager.task_management_api.service.TaskService;
import com.taskmanager.task_management_api.util.DateUtil;
import com.taskmanager.task_management_api.util.RoleUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    @Override
    public Page<TaskResponse> getTasks(Long userId, TaskStatus status, Pageable pageable) {

        Page<Task> tasks;

        if (RoleUtil.isAdmin()) {
            tasks = (status != null)
                    ? taskRepository.findAll(pageable)
                    : taskRepository.findAll(pageable);
        } else {
            tasks = (status != null)
                    ? taskRepository.findByUserIdAndStatus(userId, status, pageable)
                    : taskRepository.findByUserId(userId, pageable);
        }

        return tasks.map(this::mapToResponse);
    }

    @Override
    public TaskResponse getTaskById(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to access this task");
        }

        return mapToResponse(task);
    }

    @Override
    public TaskResponse createTask(TaskRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(
                request.getStatus() != null ? request.getStatus() : TaskStatus.TODO
        );
        task.setDueDate(request.getDueDate());
        task.setUser(user);
        task.setCreatedAt(DateUtil.now());

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTask(Long taskId, TaskRequest request, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to access this task");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        task.setUpdatedAt(DateUtil.now());

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!RoleUtil.isAdmin() && !task.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Not allowed to delete this task");
        }

        taskRepository.delete(task);
    }
}
