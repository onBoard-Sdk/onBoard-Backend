package com.onboard.server.domain.guide.domain

import org.springframework.data.jpa.repository.JpaRepository

interface GuideElementRepository : JpaRepository<GuideElement, Long> {
}
