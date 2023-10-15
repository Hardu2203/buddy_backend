package com.example.buddy_backend.user

import com.example.buddy_backend.ControllerBase
import com.example.buddy_backend.security.LoginDto
import com.example.buddy_backend.security.RefreshToken
import com.example.buddy_backend.security.SecurityService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ControllerBase.USER)
class UserController(
    val userService: UserService,
    val securityService: SecurityService
) {

    @GetMapping("{publicKey}")
    fun getOrCreateUser(@PathVariable publicKey: String): User {
        return userService.getUserOrNull(publicKey) ?: userService.createUser(publicKey)
    }

    @PostMapping("/{userId}")
    fun login(
        @PathVariable userId: String,
        @RequestParam(required = false) signedNonce: String
    ): LoginDto {
        return securityService.login(userId, signedNonce)
    }

    @PostMapping("/login-with-refresh-token")
    fun loginWithRefreshToken(
        @RequestBody refreshToken: RefreshToken,
    ): String {
        return securityService.loginWithRefreshToken(refreshToken)
    }
}

