package com.onboard.server.domain.guide.controller.dto

import com.onboard.server.domain.guide.domain.GuideElement

data class UpdateGuideElementRequest(
    val guideElements: List<GuideElement>
)
