package com.cloudapp.todoappcloudsync.security

import com.cloudapp.todoappcloudsync.utils.JwtUtil
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.SecretKey

@Configuration
class SecurityConfig(
    @Value("\${security.disableJwt:false}")
    private val disableJwt: Boolean,
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        if (disableJwt) {
            // Disable JWT: Permit all requests without JWT authentication
            http
                .csrf { it.disable() }
                .authorizeHttpRequests { it.anyRequest().permitAll() }
            return http.build()
        } else {
            http
                .csrf { it.disable() }
                .authorizeHttpRequests {
                    it
                        .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/api/v1/auth/**",
                            "/api/v1/docs/**",
                        ).permitAll()
                        .anyRequest()
                        .authenticated()
                }.oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            return http.build()
        }
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val keyBytes = jwtSecret.toByteArray(Charsets.UTF_8)
        if (keyBytes.size < 32) {
            throw IllegalStateException("jwt.secret must be at least 256 bits (32 bytes). Provided secret is too short.")
        }
        val key: SecretKey = Keys.hmacShaKeyFor(keyBytes)
        return NimbusJwtDecoder.withSecretKey(key).build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun jwtUtil(): JwtUtil {
        val keyBytes = jwtSecret.toByteArray(Charsets.UTF_8)
        if (keyBytes.size < 32) {
            throw IllegalStateException("jwt.secret must be at least 256 bits (32 bytes). Provided secret is too short.")
        }
        val key: SecretKey = Keys.hmacShaKeyFor(keyBytes)
        return JwtUtil(key)
    }
}
