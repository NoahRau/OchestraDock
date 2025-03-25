package com.cloudapp.todoappcloudsync.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Represents a user in the system")
data class UserResponse(
    @field:Schema(description = "Unique identifier of the user", example = "user-123")
    val id: String,
    @field:Schema(description = "Username of the user", example = "johndoe")
    val username: String,
)
