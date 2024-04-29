package com.onboard.server.domain.auth.controller.dto

import jakarta.validation.constraints.Email

data class SendAuthCodeRequest(
    @Email
    val email: String
)
