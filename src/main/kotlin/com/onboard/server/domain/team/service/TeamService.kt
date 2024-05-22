package com.onboard.server.domain.team.service

import com.onboard.server.domain.auth.repository.AuthCodeRepository
import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.auth.exception.NeverCertifyException
import com.onboard.server.domain.team.controller.dto.SignUpRequest
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.repository.TeamRepository
import com.onboard.server.domain.team.exception.TeamAlreadyExistsException
import com.onboard.server.global.security.jwt.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TeamService(
    private val authCodeRepository: AuthCodeRepository,
    private val teamRepository: TeamRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun singUp(request: SignUpRequest): TokenInfo {
        if (authCodeRepository.findAllByEmail(request.email)
                .all { !it.getIsVerified }
        ) throw NeverCertifyException

        if (teamRepository.existsByEmail(request.email)) throw TeamAlreadyExistsException

        val savedTeam = teamRepository.save(
            Team(
                email = request.email,
                password = passwordEncoder.encode(request.password),
            )
        )

        return jwtProvider.generateAllToken(savedTeam.id)
    }
}
