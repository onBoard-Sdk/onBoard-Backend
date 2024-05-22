package com.onboard.server.domain.team.repository

import com.onboard.server.domain.team.domain.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Team?
}
