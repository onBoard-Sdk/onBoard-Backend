package com.onboard.server.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.onboard.server.global.filter.GlobalExceptionFilter
import com.onboard.server.global.filter.JwtFilter
import com.onboard.server.global.security.jwt.JwtParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.PATCH
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
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
                    // health check
                    .requestMatchers(GET, "/health-check").permitAll()

                    // auth
                    .requestMatchers(POST, "api/v1/auth/codes").permitAll()
                    .requestMatchers(GET, "api/v1/auth/codes").permitAll()
                    .requestMatchers(POST, "api/v1/auth/sign-in").permitAll()
                    .requestMatchers(PUT, "api/v1/auth/reissue").permitAll()

                    // team
                    .requestMatchers(POST, "api/v1/teams/sign-up").permitAll()

                    // service
                    .requestMatchers(POST, "api/v1/services").authenticated()
                    .requestMatchers(PATCH, "api/v1/services/{serviceId}").authenticated()
                    .requestMatchers(DELETE, "api/v1/services/{serviceId}").authenticated()
                    .requestMatchers(GET, "api/v1/services").authenticated()

                    // file
                    .requestMatchers(POST, "api/v1/files").authenticated()

                    // guide
                    .requestMatchers(POST, "api/v1/guides").authenticated()

                    .anyRequest().denyAll()
            }
            .addFilterBefore(JwtFilter(jwtParser), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(GlobalExceptionFilter(objectMapper), JwtFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
