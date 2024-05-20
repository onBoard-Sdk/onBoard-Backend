package com.onboard.server.domain.service

import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.team.domain.Team

fun createService(
    team: Team,
    name: String = "onBoard",
    logoImageUrl: String = "default.jpg",
    serviceUrl: String = "localhost",
) = Service(
    team = team,
    name = name,
    logoImageUrl = logoImageUrl,
    serviceUrl = serviceUrl
)
