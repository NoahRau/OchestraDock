package com.cloudapp.todoappcloudsync

import com.cloudapp.todoappcloudsync.dto.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIT(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `register, login and create task`() {
        val registerResponse = restTemplate.postForEntity(
            "/api/v1/auth/register",
            RegistrationRequest("testuser", "password"),
            UserResponse::class.java
        )
        assertEquals(HttpStatus.CREATED, registerResponse.statusCode)

        val loginResponse = restTemplate.postForEntity(
            "/api/v1/auth/login",
            LoginRequest("testuser", "password"),
            Map::class.java
        )
        assertEquals(HttpStatus.OK, loginResponse.statusCode)

        val token = loginResponse.body?.get("token").toString()

        val taskHeaders = HttpHeaders().apply {
            setBearerAuth(token)
        }
        val taskRequest = HttpEntity(TaskRequest("Integration task", false), taskHeaders)
        val taskResponse = restTemplate.postForEntity(
            "/api/v1/tasks",
            taskRequest,
            TaskResponse::class.java
        )
        assertEquals(HttpStatus.CREATED, taskResponse.statusCode)
        assertEquals("Integration task", taskResponse.body?.description)
    }
}
