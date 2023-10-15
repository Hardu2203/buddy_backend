package com.example.buddy_backend.security


import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class AuthenticationFacadeImpl : AuthenticationFacade {
    override fun getAuthentication(): SecurityContextUser {
        return SecurityContextHolder
                .getContext()
                .authentication as SecurityContextUser
    }

//    private fun toAuthUser(auth: Authentication?): SecurityContextUser {
//        return if (auth != null && auth is SecurityContextUser) {
//            auth
//        } else if (auth is AbstractAuthenticationToken) {
//            return auth.toAuthUser(emptyList(), emptySet())
//        } else {
//            throw AuthenticationCredentialsNotFoundException("User is not authenticated")
//        }
//    }
}
