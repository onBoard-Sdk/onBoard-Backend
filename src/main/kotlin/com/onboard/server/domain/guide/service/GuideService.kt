package com.onboard.server.domain.guide.service

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.CreateGuideResponse
import com.onboard.server.domain.guide.domain.Guide
import com.onboard.server.domain.guide.domain.GuideElement
import com.onboard.server.domain.guide.domain.GuideElementRepository
import com.onboard.server.domain.guide.domain.GuideRepository
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.team.domain.Subject
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class GuideService(
    private val guideRepository: GuideRepository,
    private val guideElementRepository: GuideElementRepository,
    private val serviceRepository: ServiceRepository,
) {
    @Transactional
    fun create(subject: Subject, request: CreateGuideRequest): CreateGuideResponse {
        val service = serviceRepository.findByIdOrNull(request.serviceId)
            ?: throw ServiceNotFoundException

        val guide = Guide(
            service = service,
            title = request.guideTitle,
            path = request.path,
        ).apply { checkCreatable(subject.id) }

        request.guideElements
            .map { it.sequence }
            .apply { GuideElement.checkSequenceUnique(this) }

        val savedGuide = guideRepository.save(guide)

        val guideElements = request.guideElements
            .map {
                GuideElement(
                    guide = savedGuide,
                    sequence = it.sequence,
                    summary = it.emoji,
                    title = it.guideElementTitle,
                    description = it.description,
                    guideElementImageUrl = it.imageUrl,
                    shape = it.shape,
                    width = it.width,
                    length = it.length
                )
            }

        guideElementRepository.saveAll(guideElements)

        return CreateGuideResponse(savedGuide.id)
    }
}
