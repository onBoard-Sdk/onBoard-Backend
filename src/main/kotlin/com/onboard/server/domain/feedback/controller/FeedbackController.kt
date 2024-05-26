package com.onboard.server.domain.feedback.controller

import com.onboard.server.domain.feedback.controller.dto.GetAllFeedbacksResponse
import com.onboard.server.domain.feedback.controller.dto.WriteFeedbackRequest
import com.onboard.server.domain.feedback.service.FeedbackService
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.global.common.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RequestMapping("/api/v1/feedbacks")
@RestController
class FeedbackController(
    private val feedbackService: FeedbackService,
) {
    @PostMapping
    fun write(@RequestBody @Valid request: WriteFeedbackRequest) {
        feedbackService.write(request)
    }

    @GetMapping("/{serviceId}")
    fun getAll(subject: Subject, @PathVariable @NotNull serviceId: Long): ApiResponse<GetAllFeedbacksResponse> =
        ApiResponse.ok(feedbackService.getAll(subject, serviceId))
}
