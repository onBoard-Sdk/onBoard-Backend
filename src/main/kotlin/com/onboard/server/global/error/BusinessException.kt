package com.onboard.server.global.error

abstract class BusinessException(
    val status: Int,
    override val message: String
) : RuntimeException()
