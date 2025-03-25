package com.cloudapp.todoappcloudsync.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import javax.crypto.SecretKey

class JwtUtil(
    private val key: SecretKey,
) {
    fun generateToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + 3600000) // 1 hour validity
        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}
