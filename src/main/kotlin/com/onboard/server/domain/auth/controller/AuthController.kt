package com.onboard.server.domain.auth.controller

import com.onboard.server.domain.auth.controller.dto.SendAuthCodeRequest
import com.onboard.server.domain.auth.controller.dto.SignInRequest
import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

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
    fun certifyAuthCode(@RequestParam authCode: String, @RequestParam email: String) {
        authService.certifyAuthCode(authCode, email)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: SignInRequest): TokenInfo =
        authService.signIn(request.email, request.password)
}