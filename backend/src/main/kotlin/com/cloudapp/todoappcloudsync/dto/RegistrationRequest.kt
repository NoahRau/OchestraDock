package com.cloudapp.todoappcloudsync.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Registration request payload")
data class RegistrationRequest(
    @field:Schema(description = "Username for registration", example = "johndoe")
    val username: String,
    @field:Schema(description = "Password for registration", example = "securePass123")
    val password: String,
)
