package com.onboard.server.domain.guide.controller.dto

import com.onboard.server.domain.guide.repository.vo.AllGuidesWithElementsVO
import com.onboard.server.domain.guide.repository.vo.GuideElementVO
import com.onboard.server.domain.guide.repository.vo.GuideVO

data class GetAllGuidesWithElementsResponse(
    val guide: GuideVO,
    val guideElements: List<GuideElementVO>,
) {
    companion object {
        fun from(guidesWithElements: AllGuidesWithElementsVO): GetAllGuidesWithElementsResponse {
            return guidesWithElements.run {
                GetAllGuidesWithElementsResponse(
                    guide = guideVO,
                    guideElements = guideElementVOs,
                )
            }
        }
    }
}
