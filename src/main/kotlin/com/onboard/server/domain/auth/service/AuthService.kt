package com.onboard.server.domain.auth.service

import com.onboard.server.domain.auth.domain.AuthCode
import com.onboard.server.domain.auth.domain.AuthCodeRepository
import com.onboard.server.domain.auth.domain.RefreshToken
import com.onboard.server.domain.auth.domain.RefreshTokenRepository
import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.auth.exception.AuthCodeNotFoundException
import com.onboard.server.domain.auth.exception.RefreshTokenNotFoundException
import com.onboard.server.domain.auth.exception.WrongAuthInfoException
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamAlreadyExistsException
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
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun sendAuthCode(email: String) {
        if (teamRepository.existsByEmail(email)) throw TeamAlreadyExistsException

        authCodeRepository.findAllByEmail(email).size
            .apply { AuthCode.checkMaxRequestLimit(this) }

        val savedAuthCode = authCodeRepository.save(
            AuthCode(
                code = AuthCode.generateRandomCode(),
                email = email,
            )
        )

        emailService.send(savedAuthCode.code, savedAuthCode.email)
    }

    fun certifyAuthCode(code: String, email: String) {
        val authCode = authCodeRepository.findByIdOrNull(code)
            ?.apply { certify(email) }
            ?: throw AuthCodeNotFoundException

        authCodeRepository.save(authCode)
    }

    fun signIn(email: String, password: String): TokenInfo {
        val team = teamRepository.findByEmail(email)
            ?.apply {
                if (!passwordEncoder.matches(password, this.password)) throw WrongAuthInfoException
            } ?: throw WrongAuthInfoException

        return jwtProvider.generateAllToken(team.id).apply {
            refreshTokenRepository.save(
                RefreshToken(
                    userId = team.id,
                    token = refreshToken,
                )
            )
        }
    }

    fun reissue(refreshToken: String): TokenInfo {
        val savedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
            ?: throw RefreshTokenNotFoundException

        return jwtProvider.generateAllToken(savedRefreshToken.userId).apply {
            refreshTokenRepository.save(
                savedRefreshToken.updateToken(this.refreshToken)
            )
        }
    }
}
