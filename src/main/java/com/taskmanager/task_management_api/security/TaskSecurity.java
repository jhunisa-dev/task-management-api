package com.taskmanager.task_management_api.security;

import com.taskmanager.task_management_api.repository.TaskRepository;
import com.taskmanager.task_management_api.util.SecurityUtil;
import org.springframework.stereotype.Component;

@Component("taskSecurity")
public class TaskSecurity {

    private final TaskRepository taskRepository;

    public TaskSecurity(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean isOwner(Long taskId) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return taskRepository.findById(taskId)
                // Use equalsIgnoreCase to prevent strict casing bugs
                .map(task -> task.getUser().getUsername().equalsIgnoreCase(currentUsername))
                .orElse(false);
    }
}