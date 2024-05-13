package com.onboard.server.domain.auth.domain

import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?
}
