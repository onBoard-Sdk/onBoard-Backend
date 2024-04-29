package com.onboard.server.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.onboard.server.global.filter.GlobalExceptionFilter
import com.onboard.server.global.filter.JwtFilter
import com.onboard.server.global.security.jwt.JwtParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtParser: JwtParser,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "api/v1/auth/codes").permitAll()
                    .requestMatchers(HttpMethod.GET, "api/v1/auth/codes").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JwtFilter(jwtParser), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(GlobalExceptionFilter(objectMapper), JwtFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
