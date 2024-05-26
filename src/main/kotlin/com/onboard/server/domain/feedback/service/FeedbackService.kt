package com.onboard.server.domain.feedback.service

import com.onboard.server.domain.feedback.controller.dto.WriteFeedbackRequest
import com.onboard.server.domain.feedback.domain.Feedback
import com.onboard.server.domain.feedback.repository.FeedbackRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.service.repository.ServiceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val serviceRepository: ServiceRepository,
) {
    @Transactional
    fun write(request: WriteFeedbackRequest) {
        val service = serviceRepository.findByIdOrNull(request.serviceId)
            ?: throw ServiceNotFoundException

        feedbackRepository.save(
            Feedback(
                service = service,
                title = request.title,
                content = request.content
            )
        )
    }
}
