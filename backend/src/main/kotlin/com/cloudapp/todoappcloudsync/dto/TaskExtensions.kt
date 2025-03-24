package com.cloudapp.todoappcloudsync.dto

import com.cloudapp.todoappcloudsync.model.Task

fun Task.toResponse(): TaskResponse {
    return TaskResponse(
        id = this.id ?: "N/A",
        description = this.description,
        completed = this.completed,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
