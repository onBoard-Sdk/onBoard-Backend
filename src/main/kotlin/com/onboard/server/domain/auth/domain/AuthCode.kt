package com.onboard.server.domain.auth.domain

import com.onboard.server.domain.auth.exception.WrongAuthCodeException
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import kotlin.random.Random

@RedisHash(timeToLive = 600)
class AuthCode(
    @Id
    val code: String,

    val email: String,

    val isVerified: Boolean = false,
) {
    fun checkMine(email: String) {
        if (this.email != email) {
            throw WrongAuthCodeException
        }
    }

    companion object {
        const val CODE_LENGTH = 6

        fun generateRandomCode() = StringBuffer().apply {
            repeat(CODE_LENGTH) {
                append(Random.nextInt(9))
            }
        }.toString()
    }
}
