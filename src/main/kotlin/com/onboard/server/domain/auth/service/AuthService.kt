package com.onboard.server.domain.auth.service

import com.onboard.server.domain.auth.domain.AuthCode
import com.onboard.server.domain.auth.domain.AuthCodeRepository
import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.auth.exception.AuthCodeNotFoundException
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamAlreadyExistsException
import com.onboard.server.domain.team.exception.TeamNotFoundException
import com.onboard.server.global.security.jwt.JwtProvider
import com.onboard.server.thirdparty.email.EmailService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authCodeRepository: AuthCodeRepository,
    private val emailService: EmailService,
    private val teamRepository: TeamRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) {
    fun sendAuthCode(email: String) {
        if (teamRepository.existsByEmail(email)) {
            throw TeamAlreadyExistsException
        }

        authCodeRepository.countByEmail(email)?.let {
            AuthCode.checkMaxRequestLimit(it)
        }

        val savedAuthCode = authCodeRepository.save(
            AuthCode(
                code = AuthCode.generateRandomCode(),
                email = email,
            )
        )

        emailService.send(savedAuthCode.code, savedAuthCode.email)
    }

    fun certifyAuthCode(code: String, email: String) {
        val authCode = (authCodeRepository.findByIdOrNull(code)
            ?.apply { checkMine(email) }
            ?: throw AuthCodeNotFoundException)

        authCodeRepository.save(authCode.certify())
    }

    fun signIn(email: String, password: String): TokenInfo {
        val team = teamRepository.findByEmail(email)
            ?.apply { passwordEncoder.matches(password, this.password) }
            ?: throw TeamNotFoundException

        return jwtProvider.generateAllToken(team.id)
    }
}
