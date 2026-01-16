package com.taskmanager.task_management_api.dto;

import com.taskmanager.task_management_api.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private TaskStatus status;

    private LocalDate dueDate;
}
