package com.onboard.server.domain.team.exception

import com.onboard.server.global.error.BusinessException

object TeamNotFoundException : BusinessException(404, "팀을 찾지 못했습니다.") {
    private fun readResolve(): Any = TeamNotFoundException
}

object TeamAlreadyExistsException : BusinessException(409, "팀이 이미 존재합니다.") {
    private fun readResolve(): Any = TeamAlreadyExistsException
}
