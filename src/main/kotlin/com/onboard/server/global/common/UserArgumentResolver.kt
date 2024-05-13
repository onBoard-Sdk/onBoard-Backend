package com.onboard.server.global.common

import com.onboard.server.domain.team.domain.CurrentTeam
import com.onboard.server.global.security.jwt.JwtParser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(
    private val jwtParser: JwtParser,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == CurrentTeam::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val request = webRequest.nativeRequest as HttpServletRequest

        val userId = jwtParser.getToken(request)?.let {
            jwtParser.getSubject(it)
        }

        return CurrentTeam(userId!!)
    }
}
