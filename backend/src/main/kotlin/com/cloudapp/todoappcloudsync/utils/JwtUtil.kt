package com.cloudapp.todoappcloudsync.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import javax.crypto.SecretKey

class JwtUtil(
    private val key: SecretKey,
    // 1 hour validity by default
    private val expirationMillis: Long = 3600000,
) {
    fun generateToken(username: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationMillis)
        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Claims =
        Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}
