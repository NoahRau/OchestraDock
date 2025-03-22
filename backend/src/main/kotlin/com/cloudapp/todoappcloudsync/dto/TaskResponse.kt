package com.cloudapp.todoappcloudsync.dto

import java.time.LocalDateTime

data class TaskResponse(
    val id: String,
    val description: String,
    val completed: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)