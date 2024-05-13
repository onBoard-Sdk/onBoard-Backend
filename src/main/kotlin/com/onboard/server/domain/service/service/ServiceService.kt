package com.onboard.server.domain.service.service

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.controller.dto.CreateServiceResponse
import com.onboard.server.domain.service.controller.dto.GetAllServicesResponse
import com.onboard.server.domain.service.controller.dto.ModifyServiceRequest
import com.onboard.server.domain.service.controller.dto.ModifyServiceResponse
import com.onboard.server.domain.service.controller.dto.ServiceElement
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
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
    @Transactional
    fun create(subject: Subject, request: CreateServiceRequest): CreateServiceResponse {
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

    @Transactional
    fun modify(
        subject: Subject,
        serviceId: Long,
        request: ModifyServiceRequest,
    ): ModifyServiceResponse {
        teamRepository.findByIdOrNull(subject.id)
            ?: throw TeamNotFoundException

        val modifiedService = serviceRepository.findByIdOrNull(serviceId)
            ?.apply {
                modify(
                    subject = subject,
                    name = request.name,
                    logoImageUrl = request.logoImageUrl,
                    serviceUrl = request.serviceUrl
                )
            } ?: throw ServiceNotFoundException

        return ModifyServiceResponse(modifiedService.id)
    }

    @Transactional
    fun remove(subject: Subject, serviceId: Long) {
        teamRepository.findByIdOrNull(subject.id)
            ?: throw TeamNotFoundException

        serviceRepository.findByIdOrNull(serviceId)
            ?.apply { checkMine(subject) }
            ?: throw ServiceNotFoundException

        serviceRepository.deleteById(serviceId)
    }



    fun getAll(subject: Subject): GetAllServicesResponse {
        teamRepository.findByIdOrNull(subject.id)
            ?: throw TeamNotFoundException

        val services = serviceRepository.findAllByTeamId(subject.id)
            .map {
                ServiceElement(
                    name = it.getName,
                    logoImageUrl = it.getLogoImageUrl,
                    serviceUrl = it.getServiceUrl
                )
            }

        return GetAllServicesResponse(services)
    }
}
