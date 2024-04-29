package com.onboard.server.global.security.jwt

import com.onboard.server.domain.auth.domain.RefreshToken
import com.onboard.server.domain.auth.domain.RefreshTokenRepository
import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.global.security.jwt.JwtConstant.ACCESS
import com.onboard.server.global.security.jwt.JwtConstant.REFRESH
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun generateAllToken(id: String): TokenInfo {
        val accessToken = generateAccessToken(id)
        val refreshToken = generateRefreshToken(id)

        refreshTokenRepository.save(
            RefreshToken(
                accountId = id,
                token = refreshToken
            )
        )

        return TokenInfo(
            accessToken,
            refreshToken,
        )
    }

    fun generateAccessToken(id: String) = generateToken(
        id = id,
        type = ACCESS,
        exp = jwtProperties.accessExp
    )

    fun generateRefreshToken(id: String) = generateToken(
        id = id,
        type = REFRESH,
        exp = jwtProperties.refreshExp
    )

    private fun generateToken(id: String, type: String, exp: Long) =
        Jwts.builder()
            .setHeaderParam(Header.TYPE, type)
            .setSubject(id)
            .signWith(jwtProperties.secretKey, SignatureAlgorithm.HS256)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + exp * 1000))
            .compact()
}
