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
import java.time.LocalDateTime
import java.util.Date

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun generateAllToken(id: Long): TokenInfo {
        val accessToken = generateAccessToken(id.toString())
        val refreshToken = generateRefreshToken(id.toString())

        refreshTokenRepository.save(
            RefreshToken(
                userId = id,
                token = refreshToken
            )
        )

        return LocalDateTime.now().let {
            TokenInfo(
                accessToken = accessToken,
                accessTokenExpirationTime = it.plusSeconds(jwtProperties.accessExp),
                refreshToken = refreshToken,
                refreshTokenExpirationTime = it.plusSeconds(jwtProperties.refreshExp)
            )
        }
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
