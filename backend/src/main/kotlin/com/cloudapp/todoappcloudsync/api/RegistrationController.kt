package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.RegistrationRequest
import com.cloudapp.todoappcloudsync.service.UserService
import com.cloudapp.todoappcloudsync.utils.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Registration", description = "Endpoints for user registration")
@RestController
@RequestMapping("/api/v1/auth")
@Validated
class RegistrationController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    @Value("\${app.registration.enabled:true}")
    private val registrationEnabled: Boolean,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(RegistrationController::class.java)
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "User registered and token returned",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """{"token": "jwt-token-here"}""",
                            ),
                    ),
                ],
            ),
            ApiResponse(responseCode = "403", description = "Registration is disabled"),
            ApiResponse(responseCode = "400", description = "User already exists or invalid data"),
        ],
    )
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegistrationRequest,
    ): ResponseEntity<Any> {
        logger.info("Received registration request for username: {}", request.username)

        if (!registrationEnabled) {
            logger.warn("Registration is disabled")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Registration is currently disabled.")
        }

        val newUser = userService.registerUser(request.username, request.password)
        logger.info("User registered successfully: {} with id: {}", newUser.username, newUser.id)

        val token = jwtUtil.generateToken(newUser.username)
        logger.info("JWT generated for new user: {}", newUser.username)

        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("token" to token))
    }
}
