package com.onboard.server.domain.team.exception

import com.onboard.server.global.error.BusinessException

object TeamNotFoundException : BusinessException(404, "팀을 찾지 못했습니다.") {
    private fun readResolve(): Any = TeamNotFoundException
}
