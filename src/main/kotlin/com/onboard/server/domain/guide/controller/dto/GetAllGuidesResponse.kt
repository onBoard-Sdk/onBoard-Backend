package com.onboard.server.domain.guide.controller.dto

import com.onboard.server.domain.guide.repository.vo.GuideVO

data class GetAllGuidesResponse(
    val guides: List<GuideVO>
)
