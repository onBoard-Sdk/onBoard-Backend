package com.onboard.server.domain.guide.controller.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CreateGuideRequest(
    @field:Positive
    @field:NotNull
    val serviceId: Long,

    @field:Size(max = 50)
    @field:NotBlank
    val guideTitle: String,

    @field:Size(max = 50)
    @field:NotBlank
    val path: String,

    @field:NotEmpty
    @field:Valid
    val guideElements: List<CreateGuideElementRequest>,
)
