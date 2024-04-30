package com.onboard.server.domain.team.service

import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.team.controller.dto.SignUpRequest
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamAlreadyExistsException
import com.onboard.server.global.security.jwt.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val teamRepository: TeamRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
) {
    fun singUp(request: SignUpRequest): TokenInfo {
        if (teamRepository.existsByEmail(request.email)) {
            throw TeamAlreadyExistsException
        }

        val savedTeam = teamRepository.save(
            Team(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                name = request.name,
                logoImageUrl = request.logoImageUrl
            )
        )

        return jwtProvider.generateAllToken(savedTeam.id)
    }
}
