package com.cloudapp.todoappcloudsync.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Represents a Task in the system")
data class TaskResponse(
    @Schema(description = "Unique identifier of the task", example = "1234567890abc")
    val id: String,
    @Schema(description = "A group for tasks", example = "Example project")
    val project: String?,
    @Schema(description = "A short description of the task", example = "Buy groceries")
    val description: String,
    @Schema(description = "Flag indicating if the task is completed", example = "false")
    val completed: Boolean,
    @Schema(description = "Author of the task", example = "1234567890abc")
    val userId: String?,
    @Schema(description = "Date and time the task was created")
    val createdAt: LocalDateTime,
    @Schema(description = "Date and time the task was last updated")
    val updatedAt: LocalDateTime,
)
