package com.onboard.server.domain.guide.exception

import com.onboard.server.global.error.BusinessException

object CannotDuplicateSequenceException : BusinessException(400, "가이드 요소의 순서는 중복될 수 없습니다.") {
    private fun readResolve(): Any = CannotDuplicateSequenceException
}

object CannotCreateGuideException : BusinessException(403, "가이드를 생성할 수 없습니다.") {
    private fun readResolve(): Any = CannotCreateGuideException
}
