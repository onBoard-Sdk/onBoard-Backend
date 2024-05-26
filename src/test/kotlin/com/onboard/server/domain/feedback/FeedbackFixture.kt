package com.onboard.server.domain.feedback

import com.onboard.server.domain.feedback.domain.Feedback
import com.onboard.server.domain.service.domain.Service

fun createFeedback(
    service: Service,
    path: String = "/home",
    title: String = "피드백 제목",
    content: String = "피드백 내용",
) = Feedback(
    service = service,
    path = path,
    title = title,
    content = content
)
