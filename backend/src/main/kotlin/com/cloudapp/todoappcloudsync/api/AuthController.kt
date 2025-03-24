package com.cloudapp.todoappcloudsync.api

import com.cloudapp.todoappcloudsync.dto.LoginRequest
import com.cloudapp.todoappcloudsync.service.UserService
import com.cloudapp.todoappcloudsync.utils.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@Tag(name = "Auth", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    companion object {
        private val logger = LoggerFactory.getLogger(AuthController::class.java)
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User authenticated"),
            ApiResponse(responseCode = "401", description = "Invalid username or password")
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        logger.info("Login attempt for user: ${request.username}")
        val user = userService.findByUsername(request.username)
            ?: run {
                logger.warn("User not found: ${request.username}")
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password")
            }
        if (!bCryptPasswordEncoder.matches(request.password, user.password)) {
            logger.warn("Invalid password for user: ${request.username}")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password")
        }
        val token = jwtUtil.generateToken(user.username)
        logger.info("JWT generated for user: ${user.username}")
        return ResponseEntity.ok(mapOf("token" to token))
    }
}
