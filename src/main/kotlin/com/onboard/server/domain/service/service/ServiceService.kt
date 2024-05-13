package com.onboard.server.domain.service.service

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.controller.dto.CreateServiceResponse
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@org.springframework.stereotype.Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val teamRepository: TeamRepository,
) {
    fun create(request: CreateServiceRequest, subject: Subject): CreateServiceResponse {
        val currentTeam = teamRepository.findByIdOrNull(subject.id)
            ?: throw TeamNotFoundException

        val savedService = serviceRepository.save(
            Service(
                team = currentTeam,
                name = request.name,
                logoImageUrl = request.logoImageUrl,
                serviceUrl = request.serviceUrl
            )
        )

        return CreateServiceResponse(savedService.id)
    }
}
