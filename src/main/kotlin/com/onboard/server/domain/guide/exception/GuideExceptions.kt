package com.onboard.server.domain.guide.exception

import com.onboard.server.global.error.BusinessException

object CannotCreateGuideException : BusinessException(403, "가이드를 생성할 수 없습니다.") {
    private fun readResolve(): Any = CannotCreateGuideException
}
