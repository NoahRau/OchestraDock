package com.cloudapp.todoappcloudsync.dto

data class TaskRequest(
    val description: String,
    val completed: Boolean = false
)