package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.RegistrationRequest
import com.cloudapp.todoappcloudsync.dto.UserResponse
import com.cloudapp.todoappcloudsync.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Registration", description = "Endpoints for user registration")
@RestController
@RequestMapping("/api/v1/auth")
class RegistrationController(
    private val userService: UserService,
    @Value("\${app.registration.enabled:true}")
    private val registrationEnabled: Boolean,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(RegistrationController::class.java)
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account if registration is enabled.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered"),
            ApiResponse(responseCode = "403", description = "Registration is disabled"),
            ApiResponse(responseCode = "400", description = "User already exists or invalid data"),
        ],
    )
    @PostMapping("/register")
    fun register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration info",
            required = true
        )
        @RequestBody request: RegistrationRequest,
    ): ResponseEntity<Any> {
        logger.info("Received registration request for username: ${request.username}")
        if (!registrationEnabled) {
            logger.warn("Registration is disabled")
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Registration is currently disabled.")
        }
        val newUser = userService.registerUser(request.username, request.password)
        logger.info("User registered successfully: ${newUser.username}")
        val response =
            UserResponse(
                id = newUser.id ?: "N/A",
                username = newUser.username,
            )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
