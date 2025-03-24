package com.cloudapp.todoappcloudsync.repository

import com.cloudapp.todoappcloudsync.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): Optional<User>
}
