package com.onboard.server.domain.guide.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UpdateGuideElementRequest(
    @field:NotNull
    val guideElementId: Long,

    @field:Size(max = 191)
    val emoji: String?,

    @field:Size(max = 50)
    @field:NotBlank
    val guideElementTitle: String,

    @field:Size(max = 255)
    @field:NotBlank
    val description: String,

    @field:Size(max = 255)
    val imageUrl: String?,

    @field:Size(max = 255)
    @field:NotBlank
    val shape: String,

    @field:NotNull
    val width: Int,

    @field:NotNull
    val length: Int,
)
