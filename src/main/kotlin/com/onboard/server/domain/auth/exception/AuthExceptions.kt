package com.onboard.server.domain.auth.exception

import com.onboard.server.global.error.BusinessException

object WrongEmailException : BusinessException(401, "잘못된 이메일입니다.") {
    private fun readResolve(): Any = WrongEmailException
}

object NeverCertifyException : BusinessException(401, "인증된 이메일이 아닙니다.") {
    private fun readResolve(): Any = NeverCertifyException
}

object AuthCodeNotFoundException : BusinessException(404, "인증 코드를 찾지 못했습니다.") {
    private fun readResolve(): Any = AuthCodeNotFoundException
}

object AuthCodeAlreadyCertifyException : BusinessException(409, "이미 인증된 코드입니다.") {
    private fun readResolve(): Any = AuthCodeAlreadyCertifyException
}

object AuthCodeOverLimitException : BusinessException(429, "인증 코드 제한 횟수보다 더 많이 요청했습니다.") {
    private fun readResolve(): Any = AuthCodeOverLimitException
}
