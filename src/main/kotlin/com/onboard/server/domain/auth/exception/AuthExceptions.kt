package com.onboard.server.domain.auth.exception

import com.onboard.server.global.error.BusinessException

object WrongAuthCodeException : BusinessException(401, "잘못된 인증 코드 입니다.") {
    private fun readResolve(): Any = WrongAuthCodeException
}

object NeverCertifyException : BusinessException(401, "인증된 이메일이 아닙니다.") {
    private fun readResolve(): Any = NeverCertifyException
}

object AuthCodeNotFoundException : BusinessException(404, "인증 코드를 찾지 못했습니다.") {
    private fun readResolve(): Any = AuthCodeNotFoundException
}

object AuthCodeOverLimitException : BusinessException(429, "인증 코드 제한 횟수보다 더 많이 요청했습니다.") {
    private fun readResolve(): Any = AuthCodeOverLimitException
}
