package com.taskmanager.task_management_api.service;

import com.taskmanager.task_management_api.dto.TaskResponse;
import com.taskmanager.task_management_api.dto.TaskRequest;
import com.taskmanager.task_management_api.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskResponse> getTasks(Long userId, TaskStatus status, Pageable pageable);
    TaskResponse getTaskById(Long taskId, Long userId);
    TaskResponse createTask(TaskRequest request, Long userId);
    TaskResponse updateTask(Long taskId, TaskRequest request, Long userId);
    void deleteTask(Long taskId, Long userId);

}