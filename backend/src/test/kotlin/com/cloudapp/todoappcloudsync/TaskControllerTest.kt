package com.cloudapp.todoappcloudsync
/*

import com.cloudapp.todoappcloudsync.model.Task
import com.cloudapp.todoappcloudsync.repository.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val taskRepository: TaskRepository
) {

    @BeforeEach
    fun setup() {
        taskRepository.deleteAll()
    }

    @Test
    fun `test creating a new task`() {
        val taskJson = """
            {
              "description": "Test task",
              "completed": false
            }
        """.trimIndent()

        mockMvc.post("/api/v1/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = taskJson
        }.andExpect {
            status { isCreated() }
        }.andReturn()
    }

    @Test
    fun `test getting tasks`() {
        // Insert a task
        taskRepository.save(Task(description = "Sample", completed = false))

        // Retrieve tasks
        mockMvc.get("/api/v1/tasks")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].description") { value("Sample") }
            }
    }
}


 */