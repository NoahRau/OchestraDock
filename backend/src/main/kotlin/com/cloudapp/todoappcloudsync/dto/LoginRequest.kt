package com.cloudapp.todoappcloudsync.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

@Schema(description = "Login request payload")
data class LoginRequest(
    @field:NotEmpty(message = "Username must not be empty")
    @field:Schema(description = "Username of the user", example = "johndoe")
    val username: String,
    @field:NotEmpty(message = "Password must not be empty")
    @field:Schema(description = "Password of the user", example = "password123")
    val password: String,
)
