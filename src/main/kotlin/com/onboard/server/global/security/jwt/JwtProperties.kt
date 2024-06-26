package com.onboard.server.global.security.jwt

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties(
    secretKey: String,
    val accessExp: Long,
    val refreshExp: Long
) {
    val secretKey = Keys.hmacShaKeyFor(secretKey.toByteArray(Charsets.UTF_8))
}
