package com.onboard.server.domain.feedback.controller.dto

import com.onboard.server.domain.feedback.domain.Feedback

data class GetAllFeedbacksResponse(
    val feedbacks: List<FeedbackElement>,
) {
    companion object {
        fun from(feedbacks: List<Feedback>) = GetAllFeedbacksResponse(
            feedbacks.map {
                FeedbackElement(
                    path = it.path,
                    title = it.title,
                    content = it.content,
                )
            }
        )
    }
}
