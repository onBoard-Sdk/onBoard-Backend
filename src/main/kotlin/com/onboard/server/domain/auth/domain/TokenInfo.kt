package com.onboard.server.domain.auth.domain

import java.time.LocalDateTime

data class TokenInfo(
    val accessToken: String,
    val accessTokenExpirationTime: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpirationTime: LocalDateTime,
)
