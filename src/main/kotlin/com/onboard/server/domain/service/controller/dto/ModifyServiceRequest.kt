package com.onboard.server.domain.service.controller.dto

data class ModifyServiceRequest(
    val name: String,
    val logoImageUrl: String, // TODO 기본 이미지 추가
    val serviceUrl: String
)
