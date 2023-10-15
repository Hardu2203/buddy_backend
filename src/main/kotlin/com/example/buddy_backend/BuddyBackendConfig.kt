package com.example.buddy_backend

import com.example.buddy_backend.security.AuthenticationFacade
import com.example.buddy_backend.security.AuthenticationFacadeImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BuddyBackendConfig {

    @Bean
    @ConditionalOnMissingBean
    fun defaultAuthenticationImpl(): AuthenticationFacade {
        return AuthenticationFacadeImpl()
    }

}
