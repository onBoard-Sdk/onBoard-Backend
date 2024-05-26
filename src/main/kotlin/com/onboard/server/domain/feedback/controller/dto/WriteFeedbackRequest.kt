package com.onboard.server.domain.feedback.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class WriteFeedbackRequest(
    @field:NotNull
    val serviceId: Long,

    @field:Size(max = 50)
    @field:NotBlank
    val path: String,

    @field:Size(max = 50)
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,
)
