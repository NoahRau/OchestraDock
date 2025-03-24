package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.model.User
import com.cloudapp.todoappcloudsync.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@Tag(name = "Users", description = "User management endpoints")
@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }

    @Operation(summary = "Create a user", description = "Creates a new user with the given username and password.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User successfully created"),
            ApiResponse(responseCode = "400", description = "Invalid request data")
        ]
    )
    @PostMapping
    fun createUser(@RequestParam username: String, @RequestParam password: String): User {
        logger.info("Creating user: $username")
        val user = userService.createUser(username, password)
        logger.info("User created: ${user.username} with id: ${user.id}")
        return user
    }

    @Operation(summary = "Retrieve user by ID", description = "Returns the user with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): User {
        logger.info("Fetching user with id: $id")
        return userService.getUserById(id)
    }
}
