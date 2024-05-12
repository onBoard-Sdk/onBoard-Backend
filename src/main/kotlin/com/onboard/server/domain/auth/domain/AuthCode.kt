package com.onboard.server.domain.auth.domain

import com.onboard.server.domain.auth.exception.AuthCodeAlreadyCertifyException
import com.onboard.server.domain.auth.exception.AuthCodeOverLimitException
import com.onboard.server.domain.auth.exception.WrongEmailException
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
    val getIsVerified
        get() = isVerified

    fun certify(email: String): AuthCode {
        if (isVerified) throw AuthCodeAlreadyCertifyException
        checkMine(email)

        isVerified = true
        return this
    }

    private fun checkMine(email: String) {
        if (this.email != email) throw WrongEmailException
    }

    companion object {
        const val CODE_LENGTH = 6
        const val MAX_REQUEST_LIMIT = 5

        fun generateRandomCode() = StringBuffer().apply {
            repeat(CODE_LENGTH) {
                append(Random.nextInt(9))
            }
        }.toString()

        fun checkMaxRequestLimit(count: Int) {
            if (count > MAX_REQUEST_LIMIT) throw AuthCodeOverLimitException
        }
    }
}
