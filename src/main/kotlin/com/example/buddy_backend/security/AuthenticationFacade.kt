package com.example.buddy_backend.security


interface AuthenticationFacade {
    fun getAuthentication(): SecurityContextUser
}
