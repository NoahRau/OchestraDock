package com.cloudapp.todoappcloudsync.repository

import com.cloudapp.todoappcloudsync.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findAllByUsername(username: String): List<User>
}
