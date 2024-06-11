package com.onboard.server.domain.guide.domain

import org.springframework.data.jpa.repository.JpaRepository

interface GuideJpaRepository : JpaRepository<Guide, Long> {
    fun findAllByPath(path: String): List<Guide>

    fun findAllByServiceId(serviceId: Long): List<Guide>
}
