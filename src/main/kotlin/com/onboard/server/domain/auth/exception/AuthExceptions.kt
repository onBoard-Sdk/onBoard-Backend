package com.onboard.server.domain.auth.exception

import com.onboard.server.global.error.BusinessException

object WrongAuthCodeException : BusinessException(401, "잘못된 인증 코드 입니다.") {
    private fun readResolve(): Any = WrongAuthCodeException
}

object AuthCodeNotFoundException : BusinessException(404, "인증 코드를 찾지 못했습니다.") {
    private fun readResolve(): Any = AuthCodeNotFoundException
}
