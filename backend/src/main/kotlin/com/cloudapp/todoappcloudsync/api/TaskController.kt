package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.TaskRequest
import com.cloudapp.todoappcloudsync.dto.TaskResponse
import com.cloudapp.todoappcloudsync.service.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = ["http://localhost:3001"])
@RestController
@RequestMapping("/api/v1/tasks")
@Validated
class TaskController(
    private val taskService: TaskService,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(TaskController::class.java)
    }

    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks in the system.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
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
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @GetMapping("/{id}")
    fun getTask(
        @Parameter(description = "ID of the task to fetch", example = "1234567890abc")
        @PathVariable id: String,
    ): ResponseEntity<TaskResponse> {
        logger.info("Fetching task with id: {}", id)
        return ResponseEntity.ok(taskService.getTaskById(id))
    }

    @Operation(summary = "Create a new task", description = "Creates a new task with the given data.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Task created"),
            ApiResponse(responseCode = "400", description = "Invalid request data"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @PostMapping
    fun createTask(
        @Parameter(description = "ID of the user creating the task", example = "user123")
        @RequestParam userId: String,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Task data to create",
            required = true,
        )
        @Valid
        @RequestBody taskRequest: TaskRequest,
    ): ResponseEntity<TaskResponse> {
        logger.info("Creating task for userId: {} with description: {}", userId, taskRequest.description)
        val response = taskService.createTask(taskRequest, userId = userId)
        logger.info("Task created with id: {}", response.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Update an existing task", description = "Updates the entire task with new data.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task updated"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @PutMapping("/{id}")
    fun updateTask(
        @Parameter(description = "ID of the task to update", example = "1234567890abc")
        @PathVariable id: String,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated task data",
            required = true,
        )
        @Valid
        @RequestBody taskRequest: TaskRequest,
    ): ResponseEntity<TaskResponse> {
        logger.info("Updating task with id: {}", id)
        val response = taskService.updateTask(id, taskRequest)
        logger.info("Task updated with id: {}", id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete a task", description = "Removes the task with the given ID from the system.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Task deleted"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @DeleteMapping("/{id}")
    fun deleteTask(
        @Parameter(description = "ID of the task to delete", example = "1234567890abc")
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        logger.info("Deleting task with id: {}", id)
        taskService.deleteTask(id)
        logger.info("Task deleted with id: {}", id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Partially update a task", description = "Updates certain fields of an existing task.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task partially updated"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @PatchMapping("/{id}")
    fun partialUpdateTask(
        @Parameter(description = "ID of the task to update", example = "1234567890abc")
        @PathVariable id: String,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Map of fields to update",
            required = true,
        )
        @RequestBody updates: Map<String, Any>,
    ): ResponseEntity<TaskResponse> {
        logger.info("Partially updating task with id: {} with updates: {}", id, updates)
        val response = taskService.partialUpdateTask(id, updates)
        logger.info("Task partially updated with id: {}", id)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "List tasks by completion status",
        description = "Fetches tasks by whether they are completed or not.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @GetMapping("/status")
    fun getTasksByCompletionStatus(
        @Parameter(description = "Completion status to filter tasks by", example = "true")
        @RequestParam completed: Boolean,
    ): ResponseEntity<List<TaskResponse>> {
        logger.info("Fetching tasks with completed status: {}", completed)
        return ResponseEntity.ok(taskService.getTasksByCompletionStatus(completed))
    }

    @Operation(summary = "Retrieve all tasks by Project", description = "Returns the tasks with the given Project.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @GetMapping("/project")
    fun getTaskByProject(
        @Parameter(description = "Project of the tasks to fetch", example = "1234567890abc")
        @RequestParam project: String,
    ):  ResponseEntity<List<TaskResponse>> {
        logger.info("Fetching task from Project: {}", project)
        return ResponseEntity.ok(taskService.getTaskByProject(project))
    }
}
