package com.cloudapp.todoappcloudsync.security

import com.cloudapp.todoappcloudsync.utils.JwtUtil
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {
    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            try {
                val claims: Claims = jwtUtil.validateToken(token)
                val username = claims.subject
                if (username != null) {
                    // Add username to MDC for logging
                    MDC.put("username", username)
                    val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (ex: Exception) {
                logger.error("JWT validation failed", ex)
            }
        }
        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
