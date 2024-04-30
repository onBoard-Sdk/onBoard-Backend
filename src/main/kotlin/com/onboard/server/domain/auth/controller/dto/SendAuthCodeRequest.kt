package com.onboard.server.domain.auth.controller.dto

import jakarta.validation.constraints.Email

data class SendAuthCodeRequest(
    @field:Email
    val email: String
)
