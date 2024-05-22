package com.onboard.server.domain.guide.repository.vo

data class GuideElementVO(
    val guideElementId: Long,
    val sequence: Int,
    val emoji: String?,
    val guideElementTitle: String,
    val description: String,
    val guideElementImageUrl: String?,
    val shape: String,
    val width: Int,
    val length: Int,
)
