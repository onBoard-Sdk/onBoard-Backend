package com.onboard.server.domain.guide.service

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.CreateGuideResponse
import com.onboard.server.domain.guide.controller.dto.GetAllGuidesResponse
import com.onboard.server.domain.guide.controller.dto.GetAllGuidesWithElementsResponse
import com.onboard.server.domain.guide.controller.dto.UpdateGuideRequest
import com.onboard.server.domain.guide.controller.dto.UpdateGuideResponse
import com.onboard.server.domain.guide.domain.Guide
import com.onboard.server.domain.guide.domain.GuideElement
import com.onboard.server.domain.guide.domain.GuideElementJpaRepository
import com.onboard.server.domain.guide.domain.GuideJpaRepository
import com.onboard.server.domain.guide.exception.GuideNotFoundException
import com.onboard.server.domain.guide.repository.GuideRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.service.repository.ServiceRepository
import com.onboard.server.domain.team.domain.Subject
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class GuideService(
    private val guideJpaRepository: GuideJpaRepository,
    private val guideElementJpaRepository: GuideElementJpaRepository,
    private val serviceRepository: ServiceRepository,
    private val guideRepository: GuideRepository,
) {
    @Transactional
    fun create(subject: Subject, request: CreateGuideRequest): CreateGuideResponse {
        val service = serviceRepository.findByIdOrNull(request.serviceId)
            ?: throw ServiceNotFoundException

        val guide = Guide(
            service = service,
            title = request.guideTitle,
            path = request.path,
        ).apply { checkMine(subject.id) }

        val savedGuide = guideJpaRepository.save(guide)

        var sequence = 0
        val guideElements = request.guideElements
            .map {
                sequence++
                GuideElement(
                    guide = savedGuide,
                    sequence = sequence,
                    summary = it.emoji,
                    title = it.guideElementTitle,
                    description = it.description,
                    guideElementImageUrl = it.imageUrl,
                    shape = it.shape,
                    width = it.width,
                    length = it.length
                )
            }
        guideElementJpaRepository.saveAll(guideElements)

        return CreateGuideResponse(savedGuide.id)
    }

    @Transactional
    fun modify(subject: Subject, guideId: Long, request: UpdateGuideRequest): UpdateGuideResponse {
        val guide = guideJpaRepository.findByIdOrNull(guideId)
            ?.apply {
                checkMine(subject.id)
                update(request.guideTitle, request.path)
            } ?: throw GuideNotFoundException

        val savedGuideElements = guideElementJpaRepository.findAllByGuideId(guideId)
        guideElementJpaRepository.deleteAll(savedGuideElements)

        var sequence = 0
        val guideElements = request.guideElements
            .map {
                sequence++
                GuideElement(
                    guide = guide,
                    sequence = sequence,
                    summary = it.emoji,
                    title = it.guideElementTitle,
                    description = it.description,
                    guideElementImageUrl = it.imageUrl,
                    shape = it.shape,
                    width = it.width,
                    length = it.length
                )
            }
        guideElementJpaRepository.saveAll(guideElements)

        return UpdateGuideResponse(guide.id)
    }

    fun getAll(subject: Subject): GetAllGuidesResponse {
        val guideVOs = guideRepository.getAllByTeamId(subject.id)
        return GetAllGuidesResponse(guideVOs)
    }

    fun getAllGuideElements(guideId: Long): GetAllGuidesWithElementsResponse {
        val guidesWithElements = guideRepository.getAllWithElementsByGuideId(guideId)
            ?.apply { guideElementVOs.sortWith(compareBy { it.sequence }) }
            ?: throw GuideNotFoundException

        return GetAllGuidesWithElementsResponse.from(guidesWithElements)
    }

    fun getAll(path: String): GetAllGuidesResponse {
        val guides = guideJpaRepository.findAllByPath(path)
        return GetAllGuidesResponse.from(guides)
    }

    fun getAll(serviceId: Long, path: String): GetAllGuidesResponse {
        val guides = guideJpaRepository.findAllByServiceIdAndPath(serviceId, path)
        return GetAllGuidesResponse.from(guides)
    }
}
