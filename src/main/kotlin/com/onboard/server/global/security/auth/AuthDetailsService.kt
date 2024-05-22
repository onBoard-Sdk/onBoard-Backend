package com.onboard.server.global.security.auth

import com.onboard.server.domain.team.repository.TeamRepository
import com.onboard.server.domain.team.exception.TeamNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class AuthDetailsService(
    private val teamRepository: TeamRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val team = teamRepository.findByIdOrNull(username.toLong()) ?: throw TeamNotFoundException
        return AuthDetails(team)
    }
}
