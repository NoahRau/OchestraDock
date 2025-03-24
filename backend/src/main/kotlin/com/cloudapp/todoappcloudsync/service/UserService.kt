package com.cloudapp.todoappcloudsync.service

import com.cloudapp.todoappcloudsync.exception.ResourceNotFoundException
import com.cloudapp.todoappcloudsync.model.User
import com.cloudapp.todoappcloudsync.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }

    fun createUser(username: String, password: String): User {
        logger.info("Creating user with username: $username")
        val encodedPassword = passwordEncoder.encode(password)
        val user = User(username = username, password = encodedPassword)
        val savedUser = userRepository.save(user)
        logger.info("User saved: ${savedUser.username} with id: ${savedUser.id}")
        return savedUser
    }

    fun getUserById(id: String): User {
        logger.info("Fetching user with id: $id")
        return userRepository.findById(id).orElseThrow {
            logger.error("User with id $id not found")
            ResourceNotFoundException("User with id $id not found")
        }
    }

    fun findByUsername(username: String): User? {
        logger.info("Finding user by username: $username")
        return userRepository.findByUsername(username).orElse(null)
    }

    fun registerUser(username: String, rawPassword: String): User {
        logger.info("Registering user: $username")
        if (userRepository.findByUsername(username).isPresent) {
            logger.warn("User with username $username already exists")
            throw IllegalArgumentException("User with username $username already exists.")
        }
        val encodedPassword = passwordEncoder.encode(rawPassword)
        val user = User(username = username, password = encodedPassword)
        val savedUser = userRepository.save(user)
        logger.info("User registered successfully: ${savedUser.username} with id: ${savedUser.id}")
        return savedUser
    }
}
