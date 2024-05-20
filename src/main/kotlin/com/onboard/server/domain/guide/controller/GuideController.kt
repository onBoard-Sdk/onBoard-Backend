package com.onboard.server.domain.guide.controller

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.CreateGuideResponse
import com.onboard.server.domain.guide.controller.dto.UpdateGuideRequest
import com.onboard.server.domain.guide.controller.dto.UpdateGuideResponse
import com.onboard.server.domain.guide.service.GuideService
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.global.common.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/guides")
@RestController
class GuideController(
    private val guideService: GuideService
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(subject: Subject, @RequestBody request: CreateGuideRequest): ApiResponse<CreateGuideResponse> =
        ApiResponse.create(guideService.create(subject, request))

    @PatchMapping("/{guideId}")
    fun modify(
        subject: Subject,
        @PathVariable guideId: Long,
        @RequestBody request: UpdateGuideRequest,
    ): ApiResponse<UpdateGuideResponse> = ApiResponse.ok(guideService.modify(subject, guideId, request))
}
