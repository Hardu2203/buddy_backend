package com.example.buddy_backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun configure(http: HttpSecurity): DefaultSecurityFilterChain {

        http.cors(Customizer.withDefaults())
            .csrf()
            .disable()

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .addFilterBefore(JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        http
            .authorizeHttpRequests { authz ->
                authz.requestMatchers("/api/user/*",
                ).permitAll()
//                    .anyRequest()
//                    .authenticated()
            }


        http.headers().frameOptions().sameOrigin()

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.addAllowedOriginPattern("*")
        config.allowCredentials = true
        source.registerCorsConfiguration("/**", config)
        return source
    }


}