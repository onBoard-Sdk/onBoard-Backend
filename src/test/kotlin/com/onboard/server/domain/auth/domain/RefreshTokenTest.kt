package com.onboard.server.domain.auth.domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RefreshTokenTest : DescribeSpec({
    describe("updateToken") {
        val refreshToken = RefreshToken(
            userId = 1L,
            token = "token"
        )
        val updateToken = "updateToken"

        it("Refresh Token을 교체한다") {
            refreshToken.updateToken(updateToken)

            refreshToken.getToken shouldBe updateToken
        }
    }
})
