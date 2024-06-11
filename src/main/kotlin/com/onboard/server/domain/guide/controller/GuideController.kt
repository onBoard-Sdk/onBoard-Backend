package com.onboard.server.domain.guide.controller

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.CreateGuideResponse
import com.onboard.server.domain.guide.controller.dto.GetAllGuidesResponse
import com.onboard.server.domain.guide.controller.dto.GetAllGuidesWithElementsResponse
import com.onboard.server.domain.guide.controller.dto.UpdateGuideRequest
import com.onboard.server.domain.guide.controller.dto.UpdateGuideResponse
import com.onboard.server.domain.guide.service.GuideService
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.global.common.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@RequestMapping("/api/v1/guides")
@RestController
class GuideController(
    private val guideService: GuideService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(subject: Subject, @RequestBody request: CreateGuideRequest): ApiResponse<CreateGuideResponse> =
        ApiResponse.create(guideService.create(subject, request))

    @PatchMapping("/{guideId}")
    fun modify(
        subject: Subject,
        @PathVariable @NotNull guideId: Long,
        @RequestBody @Valid request: UpdateGuideRequest,
    ): ApiResponse<UpdateGuideResponse> = ApiResponse.ok(guideService.modify(subject, guideId, request))

    @GetMapping
    fun getAll(subject: Subject): ApiResponse<GetAllGuidesResponse> =
        ApiResponse.ok(guideService.getAll(subject))

    @GetMapping("/{guideId}/flows")
    fun getAllWithElements(@PathVariable @NotNull guideId: Long): ApiResponse<GetAllGuidesWithElementsResponse> =
        ApiResponse.ok(guideService.getAllGuideElements(guideId))

    @GetMapping("/pages")
    fun getAll(@RequestParam @NotBlank path: String): ApiResponse<GetAllGuidesResponse> =
        ApiResponse.ok(guideService.getAll(path))

    @GetMapping("/{serviceId}")
    fun getAll(
        @PathVariable serviceId: Long,
        @RequestParam(required = false) path: String?,
    ): ApiResponse<GetAllGuidesResponse> = ApiResponse.ok(guideService.getAll(serviceId, path))
}
