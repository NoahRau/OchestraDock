package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.LoginRequest
import com.cloudapp.todoappcloudsync.service.UserService
import com.cloudapp.todoappcloudsync.utils.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/v1/auth")
@Validated
class AuthController(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(AuthController::class.java)
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User authenticated"),
            ApiResponse(responseCode = "401", description = "Invalid username or password"),
        ],
    )
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<Any> {
        logger.info("Login attempt for user: {}", request.username)
        val user =
            userService.findByUsername(request.username) ?: run {
                logger.warn("User not found: {}", request.username)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")
            }
        if (!bCryptPasswordEncoder.matches(request.password, user.password)) {
            logger.warn("Invalid password for user: {}", request.username)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")
        }
        val token = jwtUtil.generateToken(user.username)
        logger.info("JWT generated for user: {}", user.username)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @Operation(summary = "Refresh Token", description = "Generates a new JWT token for an authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token refreshed"),
            ApiResponse(responseCode = "401", description = "Unauthorized: JWT token is missing or invalid"),
        ],
    )
    @PostMapping("/refresh")
    fun refreshToken(): ResponseEntity<Any> {
        val authentication = SecurityContextHolder.getContext().authentication
        logger.info("Authentication details: {}", authentication)

        if (authentication == null || authentication.name.isNullOrEmpty() || authentication.name == "anonymousUser") {
            logger.warn("No valid authenticated user found during token refresh")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No valid token provided")
        }

        val newToken = jwtUtil.generateToken(authentication.name)
        logger.info("Token refreshed for user: {}", authentication.name)
        return ResponseEntity.ok(mapOf("token" to newToken))
    }
}
