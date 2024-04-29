package com.onboard.server.domain.auth.domain

import com.onboard.server.domain.auth.domain.AuthCode.Companion.CODE_LENGTH
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AuthCodeTest : DescribeSpec({
    describe("generateRandomCode") {
        it("6자리의 무작위 숫자를 반환한다") {
            val randomCode = AuthCode.generateRandomCode()

            randomCode.length shouldBe CODE_LENGTH
        }
    }
})
