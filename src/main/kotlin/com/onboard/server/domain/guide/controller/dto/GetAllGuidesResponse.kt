package com.onboard.server.domain.guide.controller.dto

import com.onboard.server.domain.guide.domain.Guide
import com.onboard.server.domain.guide.repository.vo.GuideVO

data class GetAllGuidesResponse(
    val guides: List<GuideVO>
) {
    companion object {
        fun from(guides: List<Guide>): GetAllGuidesResponse {
            return GetAllGuidesResponse(
                guides.map {
                    GuideVO(
                        it.id,
                        it.getTitle,
                        it.getPath
                    )
                }
            )
        }
    }
}
