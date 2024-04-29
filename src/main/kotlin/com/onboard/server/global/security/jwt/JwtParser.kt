package com.onboard.server.global.security.jwt

import com.onboard.server.global.exception.InternalServerErrorException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header.JWT_TYPE
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import com.onboard.server.global.security.jwt.JwtConstant.ACCESS

@Component
class JwtParser(
    private val jwtProperties: JwtProperties,
    private val authDetailsService: AuthDetailsService
) {

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token).apply {
            if (header[JWT_TYPE] != ACCESS) {
                throw InvalidTokenException
            }
        }

        val userDetails = authDetailsService.loadUserByUsername(claims.body.subject)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
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
