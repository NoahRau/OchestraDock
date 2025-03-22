package com.cloudapp.todoappcloudsync.service

import com.cloudapp.todoappcloudsync.dto.TaskRequest
import com.cloudapp.todoappcloudsync.dto.TaskResponse
import com.cloudapp.todoappcloudsync.exception.ResourceNotFoundException
import com.cloudapp.todoappcloudsync.model.Task
import com.cloudapp.todoappcloudsync.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<TaskResponse> =
        taskRepository.findAll().map { it.toResponse() }

    fun getTaskById(id: String): TaskResponse =
        taskRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Task with id $id not found") }
            .toResponse()


    fun createTask(request: TaskRequest): TaskResponse =
        taskRepository.save(
            Task(description = request.description, completed = request.completed)
        ).toResponse()

      // This will only work if Mongo transactions are supported (replica set)
    fun updateTask(id: String, request: TaskRequest): TaskResponse {
        val existingTask = taskRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Task with id $id not found") }

        val updatedTask = existingTask.copy(
            description = request.description,
            completed = request.completed,
            updatedAt = LocalDateTime.now()
        )

        return taskRepository.save(updatedTask).toResponse()
    }


    fun deleteTask(id: String) {
        val task = taskRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Task with id $id not found") }
        taskRepository.delete(task)
    }

    // Extension function for easy conversion
    private fun Task.toResponse() :TaskResponse{
        if (id==null)throw ResourceNotFoundException("Task with id $id not found")
        return TaskResponse(
            id = id,
            description = description,
            completed = completed,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}