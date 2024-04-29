package com.onboard.server.thirdparty.email

interface EmailService {
    fun send(code: String, to: String)
}
