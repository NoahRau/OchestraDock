package com.cloudapp.todoappcloudsync

import com.cloudapp.todoappcloudsync.dto.LoginRequest
import com.cloudapp.todoappcloudsync.dto.RegistrationRequest
import com.cloudapp.todoappcloudsync.dto.TaskRequest
import com.cloudapp.todoappcloudsync.dto.TaskResponse
import com.cloudapp.todoappcloudsync.dto.UserResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test
import kotlin.test.fail

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IntegrationTestIT(
    @Autowired val restTemplate: TestRestTemplate,
) {
    companion object {
        @Container
        val mongoDBContainer = MongoDBContainer("mongo:latest")

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            if (!mongoDBContainer.isRunning) {
                mongoDBContainer.start()
            }
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
        }
    }

    private lateinit var userId: String
    private lateinit var token: String
    private lateinit var createdTaskId: String

    @Order(1)
    @Test
    fun `register user`() {
        val registerResponse =
            restTemplate.postForEntity(
                "/api/v1/auth/register",
                RegistrationRequest("testuser", "password"),
                UserResponse::class.java,
            )
        assertEquals(HttpStatus.CREATED, registerResponse.statusCode)
        userId = registerResponse.body?.id ?: fail("User ID should not be null")
    }

    @Order(2)
    @Test
    fun `login user`() {
        val loginRequest = LoginRequest("testuser", "password")
        val response =
            restTemplate.postForEntity(
                "/api/v1/auth/login",
                loginRequest,
                Any::class.java,
            )

        if (response.statusCode != HttpStatus.OK) {
            fail("Login failed: ${response.body}")
        }

        @Suppress("UNCHECKED_CAST")
        val body = response.body as? Map<String, Any> ?: fail("Expected a JSON object response")
        token = body["token"]?.toString() ?: fail("Token not found in response")
    }

    @Order(3)
    @Test
    fun `create task`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val request = HttpEntity(TaskRequest("Integration task", false), headers)

        val response =
            restTemplate.postForEntity(
                "/api/v1/tasks?userId=testuser",
                request,
                TaskResponse::class.java,
            )
        assertEquals(HttpStatus.CREATED, response.statusCode)
        createdTaskId = response.body?.id ?: fail("Created task ID should not be null")
        assertEquals("Integration task", response.body?.description)
    }

    @Order(4)
    @Test
    fun `retrieve task by ID`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val entity = HttpEntity<Void>(headers)

        val response =
            restTemplate.exchange(
                "/api/v1/tasks/$createdTaskId",
                HttpMethod.GET,
                entity,
                TaskResponse::class.java,
            )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(createdTaskId, response.body?.id)
    }

    @Order(5)
    @Test
    fun `update task (PUT)`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val request = HttpEntity(TaskRequest("Updated Integration Task", true), headers)

        val updateResponse =
            restTemplate.exchange(
                "/api/v1/tasks/$createdTaskId",
                HttpMethod.PUT,
                request,
                TaskResponse::class.java,
            )
        assertEquals(HttpStatus.OK, updateResponse.statusCode)

        val verifyResponse =
            restTemplate.exchange(
                "/api/v1/tasks/$createdTaskId",
                HttpMethod.GET,
                HttpEntity<Void>(headers),
                TaskResponse::class.java,
            )
        assertEquals("Updated Integration Task", verifyResponse.body?.description)
        assertTrue(verifyResponse.body?.completed ?: false)
    }

    @Order(6)
    @Test
    fun `partial update task (PATCH)`() {
        val headers =
            HttpHeaders().apply {
                setBearerAuth(token)
                contentType = MediaType.APPLICATION_JSON
            }
        val updates = mapOf("description" to "Partially Updated Task")
        val request = HttpEntity(updates, headers)

        restTemplate.patchForObject(
            "/api/v1/tasks/$createdTaskId",
            request,
            TaskResponse::class.java,
        )

        val verifyResponse =
            restTemplate.exchange(
                "/api/v1/tasks/$createdTaskId",
                HttpMethod.GET,
                HttpEntity<Void>(headers),
                TaskResponse::class.java,
            )
        assertEquals("Partially Updated Task", verifyResponse.body?.description)
    }

    @Order(7)
    @Test
    fun `get tasks by completion status`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val entity = HttpEntity<Void>(headers)

        val response =
            restTemplate.exchange(
                "/api/v1/tasks/status?completed=true",
                HttpMethod.GET,
                entity,
                Array<TaskResponse>::class.java,
            )
        assertEquals(HttpStatus.OK, response.statusCode)
        val tasks = response.body ?: emptyArray()
        assertTrue(tasks.any { it.id == createdTaskId })
    }

    @Order(8)
    @Test
    fun `get all tasks`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val entity = HttpEntity<Void>(headers)

        val response =
            restTemplate.exchange(
                "/api/v1/tasks",
                HttpMethod.GET,
                entity,
                Array<TaskResponse>::class.java,
            )
        assertEquals(HttpStatus.OK, response.statusCode)
        val tasks = response.body ?: emptyArray()
        assertTrue(tasks.any { it.id == createdTaskId })
    }

    @Order(9)
    @Test
    fun `delete task`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val entity = HttpEntity<Void>(headers)

        // Delete the task
        restTemplate.exchange(
            "/api/v1/tasks/$createdTaskId",
            HttpMethod.DELETE,
            entity,
            Void::class.java,
        )

        // Perform GET request and capture the ResponseEntity
        val response =
            restTemplate.exchange(
                "/api/v1/tasks/$createdTaskId",
                HttpMethod.GET,
                entity,
                String::class.java,
            )
        // Assert that the response status code is 404
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Order(10)
    @Test
    fun `get user by ID`() {
        val headers = HttpHeaders().apply { setBearerAuth(token) }
        val entity = HttpEntity<Void>(headers)

        val response =
            restTemplate.exchange(
                "/api/v1/users/$userId",
                HttpMethod.GET,
                entity,
                Map::class.java,
            )
        assertEquals(HttpStatus.OK, response.statusCode)
        val fetchedUsername = response.body?.get("username")
        assertEquals("testuser", fetchedUsername)
    }
}
