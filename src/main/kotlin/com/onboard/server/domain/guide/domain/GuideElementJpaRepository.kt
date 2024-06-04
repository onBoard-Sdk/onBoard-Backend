package com.onboard.server.domain.guide.domain

import org.springframework.data.jpa.repository.JpaRepository

interface GuideElementJpaRepository : JpaRepository<GuideElement, Long> {
    fun findAllByGuideId(guideId: Long): List<GuideElement>
}
