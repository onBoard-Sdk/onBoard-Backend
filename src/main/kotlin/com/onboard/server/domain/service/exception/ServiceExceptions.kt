package com.onboard.server.domain.service.exception

import com.onboard.server.global.error.BusinessException

object CannotAccessServiceException : BusinessException(403, "본인의 서비스가 아닙니다.") {
    private fun readResolve(): Any = CannotAccessServiceException
}

object ServiceNotFoundException : BusinessException(404, "서비스를 찾지 못했습니다.") {
    private fun readResolve(): Any = ServiceNotFoundException
}

object ServiceUrlAlreadyExistsException : BusinessException(409, "이미 존재하는 서비스 URL입니다.") {
    private fun readResolve(): Any = ServiceUrlAlreadyExistsException
}
