package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.TaskRequest
import com.cloudapp.todoappcloudsync.dto.TaskResponse
import com.cloudapp.todoappcloudsync.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3001"])
@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks(): ResponseEntity<List<TaskResponse>> =
        ResponseEntity.ok(taskService.getAllTasks())

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: String): ResponseEntity<TaskResponse> =
        ResponseEntity.ok(taskService.getTaskById(id))

    @PostMapping
    fun createTask(@RequestBody taskRequest: TaskRequest): ResponseEntity<TaskResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequest))

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: String,
        @RequestBody taskRequest: TaskRequest
    ): ResponseEntity<TaskResponse> =
        ResponseEntity.ok(taskService.updateTask(id, taskRequest))

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<Void> {
        taskService.deleteTask(id)
        return ResponseEntity.noContent().build()
    }
}