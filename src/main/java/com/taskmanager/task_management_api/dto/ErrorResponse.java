package com.taskmanager.task_management_api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
