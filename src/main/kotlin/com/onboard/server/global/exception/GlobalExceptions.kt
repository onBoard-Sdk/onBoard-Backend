package com.onboard.server.global.exception

import com.onboard.server.global.error.BusinessException

object BadRequestException : BusinessException(400, "잘못된 요청입니다.") {
    private fun readResolve(): Any = BadRequestException
}

object UnauthorizedException : BusinessException(401, "인증 정보가 잘못되었습니다.") {
    private fun readResolve(): Any = UnauthorizedException
}

object MethodNotAllowedException : BusinessException(405, "지원하지 않는 Http 메서드입니다.") {
    private fun readResolve(): Any = MethodNotAllowedException
}

object InternalServerErrorException : BusinessException(500, "서버 에러가 발생했습니다.") {
    private fun readResolve(): Any = InternalServerErrorException
}
