package com.example.buddy_backend.security

import com.example.buddy_backend.security.JwtTokenProvider.Companion.ACCESS_COOKIE_NAME
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException

class JwtTokenFilter internal constructor(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {

        val token = jwtTokenProvider.resolveToken(req as HttpServletRequest)
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                val auth: SecurityContextUser = jwtTokenProvider.getAuthentication(token)

                SecurityContextHolder.getContext().authentication = auth

            }
        } catch (e: JwtException) {
            val response = res as HttpServletResponse
            val invalidateToken = Cookie(ACCESS_COOKIE_NAME, "")
            invalidateToken.maxAge = 0
            response.addCookie(invalidateToken)

            throw e
        } catch (e: UsernameNotFoundException) {
            val response = res as HttpServletResponse
            val invalidateToken = Cookie(ACCESS_COOKIE_NAME, "")
            invalidateToken.maxAge = 0
            response.addCookie(invalidateToken)

            throw e
        }

        try {
            filterChain.doFilter(req, res)
        } finally {
            MDC.remove("userId")
        }
    }

    companion object {

        val LOGGER: Logger = LoggerFactory.getLogger(JwtTokenFilter::class.java)
    }
}
