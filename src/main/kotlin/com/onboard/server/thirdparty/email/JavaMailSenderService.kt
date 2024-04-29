package com.onboard.server.thirdparty.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class JavaMailSenderService(
    private val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    val sender: String,
) : EmailService {
    override fun send(code: String, to: String) {
        SimpleMailMessage().apply {
            from = sender
            setTo(to)
            subject = "onBoard 인증 코드입니다."
            text = code
        }
    }
}
