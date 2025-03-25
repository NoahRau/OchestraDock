package com.cloudapp.todoappcloudsync.repository

import com.cloudapp.todoappcloudsync.model.Task
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskRepository : MongoRepository<Task, String> {
    fun findByCompleted(completed: Boolean): List<Task>
}
