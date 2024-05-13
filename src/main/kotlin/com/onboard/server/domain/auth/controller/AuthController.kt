package com.onboard.server.domain.auth.controller

import com.onboard.server.domain.auth.controller.dto.SendAuthCodeRequest
import com.onboard.server.domain.auth.controller.dto.SignInRequest
import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.auth.service.AuthService
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val authService: AuthService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/codes")
    fun sendAuthCode(@RequestBody request: SendAuthCodeRequest) {
        authService.sendAuthCode(request.email)
    }

    @GetMapping("/codes")
    fun certifyAuthCode(
        @RequestParam @NotNull authCode: String,
        @RequestParam @NotNull email: String,
    ) {
        authService.certifyAuthCode(authCode, email)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: SignInRequest): TokenInfo =
        authService.signIn(request.email, request.password)

    @PutMapping("/reissue")
    fun reissue(@RequestHeader("refresh-token") @NotNull refreshToken: String): TokenInfo {
        return if (refreshToken.contains("Bearer "))
            authService.reissue(refreshToken.replace("Bearer ", ""))
        else authService.reissue(refreshToken)
    }
}
