package com.dev.server.DTOs;

import com.dev.server.enums.TaskState;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDTO {

    private String id;
    @NotBlank(message = "title field must not be empty")
    private String title;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    @NotNull(message = "finishedAt field must not be empty")
    private Instant finishAt;
    private Instant completedAt;
    private TaskState state;

}
