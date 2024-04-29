package com.onboard.server.global.security.jwt

import com.onboard.server.global.error.BusinessException


object ExpiredTokenException : BusinessException(401, "만료된 JWT입니다.")

object InvalidTokenException : BusinessException(401, "잘못된 JWT 토큰입니다.")
