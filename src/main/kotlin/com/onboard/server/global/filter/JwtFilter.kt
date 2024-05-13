package com.onboard.server.global.filter

import com.onboard.server.global.security.jwt.JwtConstant.HEADER
import com.onboard.server.global.security.jwt.JwtConstant.PREFIX
import com.onboard.server.global.security.jwt.JwtParser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtParser: JwtParser
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtParser.getToken(request)
        token?.let {
            SecurityContextHolder.getContext().authentication = jwtParser.getAuthentication(token)
        }
        filterChain.doFilter(request, response)
    }
}
