package com.onboard.server.domain.team.controller

import com.onboard.server.domain.auth.domain.TokenInfo
import com.onboard.server.domain.team.controller.dto.SignUpRequest
import com.onboard.server.domain.team.service.TeamService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/teams")
@RestController
class TeamController(
    private val teamService: TeamService
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): TokenInfo =
        teamService.singUp(request)
}
