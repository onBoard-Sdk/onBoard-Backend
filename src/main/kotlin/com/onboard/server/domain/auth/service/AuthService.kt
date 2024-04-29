package com.onboard.server.domain.auth.service

import com.onboard.server.domain.auth.domain.AuthCode
import com.onboard.server.domain.auth.domain.AuthCodeRepository
import com.onboard.server.domain.auth.exception.AuthCodeNotFoundException
import com.onboard.server.thirdparty.email.EmailService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authCodeRepository: AuthCodeRepository,
    private val emailService: EmailService,
) {
    fun sendAuthCode(email: String) {
        // TODO("이미 등록된 이메일인지 확인")

        val savedAuthCode = authCodeRepository.save(
            AuthCode(
                code = AuthCode.generateRandomCode(),
                email = email,
            )
        )

        emailService.send(savedAuthCode.code, savedAuthCode.email)
    }

    fun certifyAuthCode(code: String, email: String) {
        authCodeRepository.findByIdOrNull(code)
            ?.apply { checkMine(email) }
            ?: throw AuthCodeNotFoundException
    }
}
