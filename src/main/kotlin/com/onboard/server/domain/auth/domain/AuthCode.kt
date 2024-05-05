package com.onboard.server.domain.auth.domain

import com.onboard.server.domain.auth.exception.AuthCodeOverLimitException
import com.onboard.server.domain.auth.exception.WrongAuthCodeException
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import kotlin.random.Random

@RedisHash(timeToLive = 600)
class AuthCode(
    @Id
    val code: String,

    @Indexed
    val email: String,

    private var isVerified: Boolean = false,
) {
    val getIsVerified = isVerified

    fun certify(): AuthCode {
        isVerified = true
        return this
    }

    fun checkMine(email: String) {
        if (this.email != email) {
            throw WrongAuthCodeException
        }
    }

    companion object {
        const val CODE_LENGTH = 6
        private const val MAX_REQUEST_LIMIT = 5

        fun generateRandomCode() = StringBuffer().apply {
            repeat(CODE_LENGTH) {
                append(Random.nextInt(9))
            }
        }.toString()

        fun checkMaxRequestLimit(size: Int) {
            if (size >= MAX_REQUEST_LIMIT) throw AuthCodeOverLimitException
        }
    }
}
