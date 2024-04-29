package com.onboard.server.domain.auth.domain

data class TokenInfo(
    val accessToken: String,
    val refreshToken: String,
)
