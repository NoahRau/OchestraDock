package com.cloudapp.todoappcloudsync.service

import com.cloudapp.todoappcloudsync.dto.TaskRequest
import com.cloudapp.todoappcloudsync.dto.TaskResponse
import com.cloudapp.todoappcloudsync.dto.toResponse
import com.cloudapp.todoappcloudsync.exception.ResourceNotFoundException
import com.cloudapp.todoappcloudsync.model.Task
import com.cloudapp.todoappcloudsync.repository.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TaskService(private val taskRepository: TaskRepository) {
    companion object {
        private val logger = LoggerFactory.getLogger(TaskService::class.java)
    }

    fun getAllTasks(): List<Unit> {
        logger.info("Retrieving all tasks")
        return taskRepository.findAll().map { it.toResponse() }
    }

    fun getTaskById(id: String): TaskResponse {
        logger.info("Fetching task with id: $id")
        return taskRepository.findById(id)
            .orElseThrow {
                logger.error("Task with id $id not found")
                ResourceNotFoundException("Task with id $id not found")
            }
            .toResponse()
    }

    fun createTask(request: TaskRequest, userId: String): TaskResponse {
        logger.info("Creating task for user: $userId with description: ${request.description}")
        val savedTask = taskRepository.save(
            Task(description = request.description, completed = request.completed, userId = userId)
        )
        logger.info("Task created with id: ${savedTask.id}")
        return savedTask.toResponse()
    }

    fun updateTask(id: String, request: TaskRequest): TaskResponse {
        logger.info("Updating task with id: $id")
        val existingTask = taskRepository.findById(id)
            .orElseThrow {
                logger.error("Task with id $id not found")
                ResourceNotFoundException("Task with id $id not found")
            }
        val updatedTask = existingTask.copy(
            description = request.description,
            completed = request.completed,
            updatedAt = LocalDateTime.now()
        )
        val savedTask = taskRepository.save(updatedTask)
        logger.info("Task updated with id: $id")
        return savedTask.toResponse()
    }

    fun deleteTask(id: String) {
        logger.info("Deleting task with id: $id")
        val task = taskRepository.findById(id)
            .orElseThrow {
                logger.error("Task with id $id not found")
                ResourceNotFoundException("Task with id $id not found")
            }
        taskRepository.delete(task)
        logger.info("Task deleted with id: $id")
    }

    @Transactional
    fun partialUpdateTask(id: String, updates: Map<String, Any>): TaskResponse {
        logger.info("Partially updating task with id: $id with updates: $updates")
        val existingTask = taskRepository.findById(id)
            .orElseThrow {
                logger.error("Task with id $id not found")
                ResourceNotFoundException("Task with id $id not found")
            }
        val updatedTask = existingTask.copy(
            description = updates["description"]?.toString() ?: existingTask.description,
            completed = updates["completed"] as? Boolean ?: existingTask.completed,
            updatedAt = LocalDateTime.now()
        )
        val savedTask = taskRepository.save(updatedTask)
        logger.info("Task partially updated with id: $id")
        return savedTask.toResponse()
    }

    fun getTasksByCompletionStatus(completed: Boolean): List<Unit> {
        logger.info("Fetching tasks with completed status: $completed")
        return taskRepository.findByCompleted(completed).map { it.toResponse() }
    }
}
