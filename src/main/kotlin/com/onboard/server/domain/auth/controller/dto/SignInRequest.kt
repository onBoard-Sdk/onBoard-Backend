package com.onboard.server.domain.auth.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignInRequest(
    @field:Size(max = 60)
    @field:Email
    val email: String,

    @field:Size(min = 6, max = 20)
    @field:NotBlank
    val password: String,
)
