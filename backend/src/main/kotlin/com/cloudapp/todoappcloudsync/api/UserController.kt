package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.UserResponse
import com.cloudapp.todoappcloudsync.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }

    @Operation(summary = "Retrieve user by ID", description = "Returns the user with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @GetMapping("/{id}")
    fun getUser(
        @Parameter(description = "ID of the user to fetch", example = "user-123")
        @PathVariable id: String,
    ): ResponseEntity<UserResponse> {
        logger.info("Fetching user with id: {}", id)
        val user = userService.getUserById(id)
        val userResponse =
            UserResponse(
                id = user.id ?: "N/A",
                username = user.username,
            )
        return ResponseEntity.ok(userResponse)
    }
}
