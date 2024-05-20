package com.onboard.server.domain.guide.exception

import com.onboard.server.global.error.BusinessException

object CannotDuplicateSequenceException : BusinessException(400, "가이드 요소의 순서는 중복될 수 없습니다.") {
    private fun readResolve(): Any = CannotDuplicateSequenceException
}

object CannotCommandGuideException : BusinessException(403, "가이드를 생성 및 수정할 수 없습니다.") {
    private fun readResolve(): Any = CannotCommandGuideException
}

object GuideNotFoundException : BusinessException(404, "가이드를 찾지 못했습니다.") {
    private fun readResolve(): Any = GuideNotFoundException
}
