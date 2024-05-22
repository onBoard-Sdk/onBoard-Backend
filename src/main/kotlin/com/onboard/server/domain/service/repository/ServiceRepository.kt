package com.onboard.server.domain.service.repository

import com.onboard.server.domain.service.domain.Service
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository : JpaRepository<Service, Long> {
    fun findAllByTeamId(teamId: Long): List<Service>

    fun existsByServiceUrl(serviceUrl: String): Boolean
}
