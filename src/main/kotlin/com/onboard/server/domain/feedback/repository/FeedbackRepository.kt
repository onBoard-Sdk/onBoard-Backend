package com.onboard.server.domain.feedback.repository

import com.onboard.server.domain.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository : JpaRepository<Feedback, Long> {
}
