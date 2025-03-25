package com.cloudapp.todoappcloudsync.repository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager

@Configuration
class MongoConfig {
    @Bean
    fun mongoTransactionManager(dbFactory: MongoDatabaseFactory): MongoTransactionManager = MongoTransactionManager(dbFactory)
}
