package com.onboard.server.domain.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
class RefreshToken(
    @Id
    val userId: Long,

    @Indexed
    private var token: String
) {
    val getToken
        get() = token

    fun updateToken(token: String) {
        this.token = token
    }
}
