package com.cloudapp.todoappcloudsync.dto

import com.cloudapp.todoappcloudsync.model.Task

fun Task.toResponse(): TaskResponse =
    TaskResponse(
        id = this.id ?: "N/A",
        project = this.project,
        description = this.description,
        completed = this.completed,
        userId = this.userId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
