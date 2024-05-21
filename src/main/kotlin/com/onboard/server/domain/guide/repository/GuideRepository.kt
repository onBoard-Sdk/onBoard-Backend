package com.onboard.server.domain.guide.repository

import com.onboard.server.domain.guide.repository.vo.GuideVO

interface GuideRepository {
    fun getAllByTeamId(teamId: Long): List<GuideVO>
}
