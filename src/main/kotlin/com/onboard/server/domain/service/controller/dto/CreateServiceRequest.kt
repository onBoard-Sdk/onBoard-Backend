package com.onboard.server.domain.service.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateServiceRequest(
    @field:Size(max = 50)
    @field:NotBlank
    val name: String,

    val logoImageUrl: String, // TODO 기본 이미지 추가

    @field:NotBlank
    val serviceUrl: String
)
