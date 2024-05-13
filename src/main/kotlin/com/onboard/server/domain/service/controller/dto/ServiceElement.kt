package com.onboard.server.domain.service.controller.dto

data class ServiceElement(
    val serviceId: Long,
    val name: String,
    val logoImageUrl: String,
    val serviceUrl: String
)
