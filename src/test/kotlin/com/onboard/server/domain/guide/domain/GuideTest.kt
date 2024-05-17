package com.onboard.server.domain.guide.domain

import com.onboard.server.domain.guide.exception.CannotCreateGuideException
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.team.domain.Team
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec

class GuideTest : DescribeSpec({
    describe("checkCreatable") {
        val teamId = 1L
        val team = Team(
            id = teamId,
            email = "email",
            password = "password",
            name = "name",
            logoImageUrl = "logoImageUrl"
        )

        val service = Service(
            id = 1,
            team = team,
            name = "name",
            logoImageUrl = "logoImageUrl",
            serviceUrl = "serviceUrl",
        )

        val guide = Guide(
            id = 1,
            service = service,
            title = "홈 화면입니다.",
            path = "/home"
        )

        context("자신의 서비스가 맞다면") {
            it("가이드를 생성할 수 있다") {
                shouldNotThrowAny {
                    guide.checkCreatable(teamId)
                }
            }
        }

        context("자신의 서비스가 아니면") {
            val wrongTeamId = 2L

            it("가이드를 생성할 수 없다") {
                shouldThrow<CannotCreateGuideException> {
                    guide.checkCreatable(wrongTeamId)
                }
            }
        }
    }
})
