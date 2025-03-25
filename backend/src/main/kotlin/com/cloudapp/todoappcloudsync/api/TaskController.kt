package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.TaskRequest
import com.cloudapp.todoappcloudsync.dto.TaskResponse
import com.cloudapp.todoappcloudsync.service.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Tasks", description = "Task management endpoints")
@CrossOrigin(origins = ["http://localhost:3001"])
@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(private val taskService: TaskService) {
    companion object {
        private val logger = LoggerFactory.getLogger(TaskController::class.java)
    }

    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks in the system.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation")
        ]
    )
    @GetMapping
    fun getAllTasks(): List<TaskResponse> {
        logger.info("Fetching all tasks")
        return taskService.getAllTasks()
    }

    @Operation(summary = "Retrieve a single task by ID", description = "Returns the task with the given ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation"),
            ApiResponse(responseCode = "404", description = "Task not found")
        ]
    )
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: String): ResponseEntity<TaskResponse> {
        logger.info("Fetching task with id: $id")
        return ResponseEntity.ok(taskService.getTaskById(id))
    }

    @Operation(summary = "Create a new task", description = "Creates a new task with the given data.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Task created"),
            ApiResponse(responseCode = "400", description = "Invalid request data")
        ]
    )
    @PostMapping
    fun createTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<TaskResponse> {
        logger.info("Creating task with description: ${taskRequest.description}")
        val response = taskService.createTask(taskRequest, userId = "demoUser")
        logger.info("Task created with id: ${response.id}")
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Update an existing task", description = "Updates the entire task with new data.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task updated"),
            ApiResponse(responseCode = "404", description = "Task not found")
        ]
    )
    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: String,
        @RequestBody taskRequest: TaskRequest
    ): ResponseEntity<TaskResponse> {
        logger.info("Updating task with id: $id")
        val response = taskService.updateTask(id, taskRequest)
        logger.info("Task updated with id: $id")
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete a task", description = "Removes the task with the given ID from the system.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Task deleted"),
            ApiResponse(responseCode = "404", description = "Task not found")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("Deleting task with id: $id")
        taskService.deleteTask(id)
        logger.info("Task deleted with id: $id")
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Partially update a task", description = "Updates certain fields of an existing task.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task partially updated"),
            ApiResponse(responseCode = "404", description = "Task not found")
        ]
    )
    @PatchMapping("/{id}")
    fun partialUpdateTask(
        @PathVariable id: String,
        @RequestBody updates: Map<String, Any>
    ): ResponseEntity<TaskResponse> {
        logger.info("Partially updating task with id: $id, updates: $updates")
        val response = taskService.partialUpdateTask(id, updates)
        logger.info("Task partially updated with id: $id")
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "List tasks by completion status", description = "Fetches tasks by whether they are completed or not.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation")
        ]
    )
    @GetMapping("/status")
    fun getTasksByCompletionStatus(
        @RequestParam completed: Boolean
    ): ResponseEntity<List<TaskResponse>> { // <- Change from List<Unit>
        logger.info("Fetching tasks with completed status: $completed")
        return ResponseEntity.ok(taskService.getTasksByCompletionStatus(completed))
    }
}
