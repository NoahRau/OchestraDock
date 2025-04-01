package com.cloudapp.todoappcloudsync.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

@Schema(description = "Represents the payload for creating or updating a Task")
data class TaskRequest(
    @field:NotEmpty
    @field:Size(max = 255)
    @field:Schema(description = "A short description of the task", example = "Buy groceries")
    val description: String,
    @field:Schema(description = "Flag indicating if the task is completed", example = "false")
    val completed: Boolean = false,
    @field:Schema(description = "Flag indicating if the task is grouped in a Project", example = "example Project")
    val project: String? = null,
)
