package com.onboard.server.domain.auth

import com.onboard.server.domain.auth.domain.AuthCode

fun createAuthCode(
    code: String = "123456",
    email: String = "team@onboard.com",
    isVerified: Boolean = false,
) = AuthCode(
    code = code,
    email = email,
    isVerified = isVerified
)
