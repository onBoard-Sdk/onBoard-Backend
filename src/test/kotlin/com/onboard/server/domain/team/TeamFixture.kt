package com.onboard.server.domain.team

import com.onboard.server.domain.team.domain.Team

fun createTeam(
    email: String = "team@onboard.com",
    password: String = "password",
    name: String = "onBoard",
    logoImageUrl: String = "default.jpg",
) = Team(
    email = email,
    password = password,
    name = name,
    logoImageUrl = logoImageUrl
)
