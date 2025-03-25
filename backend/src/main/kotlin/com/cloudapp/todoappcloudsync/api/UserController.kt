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
