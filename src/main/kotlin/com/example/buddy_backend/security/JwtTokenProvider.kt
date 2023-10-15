package com.example.buddy_backend.security

import com.example.buddy_backend.user.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Component
class JwtTokenProvider @Autowired constructor(
    private val userService: UserService,
) {

    @Value("\${security.jwt.token.secret-key}")
    private var secretKey: String? = null

    @Value("\${security.jwt.token.expire-length}")
    private val validityInMilliseconds: Long = 0

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey!!.toByteArray())
    }

    internal fun createToken(
        publicKey: String
    ): String {

        val claims = Jwts.claims().setSubject(publicKey)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact()
    }


    @Transactional
    fun getAuthentication(token: String): SecurityContextUser {

        val claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)

        val principal = claimsJws.body.subject

        val principalUser = userService.getUserOrNull(principal) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        return SecurityContextUser(
                principalUser,
                setOf(),
        )
    }

    internal fun resolveToken(req: HttpServletRequest): String? {
        val cookies = req.cookies

        return if (cookies == null || cookies.isEmpty()) {
            getTokenFromBearerFormat(req)
        } else {
            cookies.toList()
                    .filter { cookie -> cookie.name == ACCESS_COOKIE_NAME }
                    .map { cookie -> cookie.value }
                    .firstOrNull() ?: getTokenFromBearerFormat(req)
        }
    }

    private fun getTokenFromBearerFormat(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }

    internal fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: JwtException) {
            throw JwtException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw JwtException("Expired or invalid JWT token")
        }
    }

    internal fun getClaims(token: String): Jws<Claims> {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        } catch (e: JwtException) {
            throw JwtException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw JwtException("Expired or invalid JWT token")
        }
    }

    companion object {

        val LOGGER: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)

        const val ACCESS_COOKIE_NAME = "access-token"
        const val EMPLOYEE_ID = "employeeId"
        const val AUTH = "auth"
        const val PUBLIC_KEY = "public-key"
    }
}
