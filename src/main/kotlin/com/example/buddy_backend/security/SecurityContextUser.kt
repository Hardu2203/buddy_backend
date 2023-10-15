package com.example.buddy_backend.security

import com.example.buddy_backend.user.User
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import java.util.*

class SecurityContextUser internal constructor(
    private val principalUser: User,
    val auths: Set<GrantedAuthority>,
) : AbstractAuthenticationToken(auths) {

    init {
        super.setAuthenticated(true)
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): User {
        return principalUser
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            throw IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"
            )
        }

        super.setAuthenticated(false)
    }


}

//fun AbstractAuthenticationToken.toAuthUser(
//    authorities: Collection<GrantedAuthority> = emptyList(),
//): SecurityContextUser {
//    return SecurityContextUser(
//        principal as Jwt,
//        authorities,
//        UUID.fromString(name),
//        (principal as Jwt).getClaim("email"),
//        organisationUuids,
//    )
//}
