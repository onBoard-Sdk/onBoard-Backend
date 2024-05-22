package com.onboard.server.domain.guide.repository.vo

data class AllGuidesWithElementsVO(
    val guideVO: GuideVO,
    val guideElementVOs: List<GuideElementVO>,
)
