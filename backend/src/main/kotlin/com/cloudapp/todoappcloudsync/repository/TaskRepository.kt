package com.cloudapp.todoappcloudsync.repository

import com.cloudapp.todoappcloudsync.model.Task
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface TaskRepository : MongoRepository<Task, String> {
    fun findByCompleted(completed: Boolean): List<Task>
}
