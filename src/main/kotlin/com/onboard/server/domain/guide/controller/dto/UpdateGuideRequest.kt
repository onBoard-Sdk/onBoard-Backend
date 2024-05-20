package com.onboard.server.domain.guide.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateGuideRequest(
    @field:Size(max = 50)
    @field:NotBlank
    val guideTitle: String,

    @field:Size(max = 50)
    @field:NotBlank
    val path: String,
)
