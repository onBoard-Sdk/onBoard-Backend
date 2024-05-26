package com.onboard.server.domain.feedback.controller

import com.onboard.server.domain.feedback.controller.dto.WriteFeedbackRequest
import com.onboard.server.domain.feedback.service.FeedbackService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/feedbacks")
@RestController
class FeedbackController(
    private val feedbackService: FeedbackService,
) {
    @PostMapping
    fun write(@RequestBody @Valid request: WriteFeedbackRequest) {
        feedbackService.write(request)
    }
}
