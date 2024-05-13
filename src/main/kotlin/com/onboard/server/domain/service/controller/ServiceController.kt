package com.onboard.server.domain.service.controller

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.controller.dto.CreateServiceResponse
import com.onboard.server.domain.service.service.ServiceService
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.global.common.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
        @RequestBody @Valid request: CreateServiceRequest,
        subject: Subject,
    ): ApiResponse<CreateServiceResponse> = ApiResponse.create(serviceService.create(request, subject))
}
