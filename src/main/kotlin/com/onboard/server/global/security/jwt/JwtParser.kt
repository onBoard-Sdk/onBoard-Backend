package com.onboard.server.global.security.jwt

import com.onboard.server.global.exception.InternalServerErrorException
import com.onboard.server.global.security.auth.AuthDetailsService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import com.onboard.server.global.security.jwt.JwtConstant.ACCESS
import io.jsonwebtoken.Header.TYPE
import jakarta.servlet.http.HttpServletRequest

@Component
class JwtParser(
    private val jwtProperties: JwtProperties,
    private val authDetailsService: AuthDetailsService
) {
    fun getAuthentication(token: String): Authentication {
        val subject = getSubject(token)

        val userDetails = authDetailsService.loadUserByUsername(subject.toString())

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getToken(request: HttpServletRequest): String? {
        val token = request.getHeader(JwtConstant.HEADER)

        return if (token != null && token.startsWith(JwtConstant.PREFIX)) {
            token.substring(JwtConstant.PREFIX.length)
        } else {
            null
        }
    }

    fun getSubject(token: String): Long {
        val claims = getClaims(token).apply {
            if (header[TYPE] != ACCESS) {
                throw InvalidTokenException
            }
        }

        return claims.body.subject.toLong()
    }

    private fun getClaims(token: String): Jws<Claims> {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(jwtProperties.secretKey)
                .build()
                .parseClaimsJws(token)
        } catch (exception: Exception) {
            when (exception) {
                is ExpiredJwtException -> throw ExpiredTokenException
                is JwtException -> throw InvalidTokenException
                else -> throw InternalServerErrorException
            }
        }
    }
}
