package com.cloudapp.todoappcloudsync.dto

import jakarta.validation.constraints.NotEmpty

data class LoginRequest(
    @field:NotEmpty(message = "Username must not be empty")
    val username: String,
    @field:NotEmpty(message = "Password must not be empty")
    val password: String,
)
