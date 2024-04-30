package com.onboard.server.domain.team.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Team?
}
