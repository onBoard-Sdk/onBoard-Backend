package com.onboard.server.domain.service.controller

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.controller.dto.CreateServiceResponse
import com.onboard.server.domain.service.controller.dto.GetAllServicesResponse
import com.onboard.server.domain.service.controller.dto.ModifyServiceRequest
import com.onboard.server.domain.service.controller.dto.ModifyServiceResponse
import com.onboard.server.domain.service.service.ServiceService
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.global.common.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/services")
@RestController
class ServiceController(
    private val serviceService: ServiceService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(
        subject: Subject,
        @RequestBody @Valid request: CreateServiceRequest,
    ): ApiResponse<CreateServiceResponse> = ApiResponse.create(serviceService.create(subject, request))

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{serviceId}")
    fun modify(
        subject: Subject,
        @PathVariable serviceId: Long,
        @RequestBody @Valid request: ModifyServiceRequest,
    ): ApiResponse<ModifyServiceResponse> = ApiResponse.noContent(serviceService.modify(subject, serviceId, request))

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{serviceId}")
    fun remove(subject: Subject, @PathVariable serviceId: Long) {
        serviceService.remove(subject, serviceId)
    }

    @GetMapping
    fun getAll(subject: Subject): ApiResponse<GetAllServicesResponse> =
        ApiResponse.ok(serviceService.getAll(subject))
}
