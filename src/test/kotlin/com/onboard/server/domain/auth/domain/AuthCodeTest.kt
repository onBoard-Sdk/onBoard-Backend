package com.onboard.server.domain.auth.domain

import com.onboard.server.domain.auth.createAuthCode
import com.onboard.server.domain.auth.domain.AuthCode.Companion.CODE_LENGTH
import com.onboard.server.domain.auth.domain.AuthCode.Companion.MAX_REQUEST_LIMIT
import com.onboard.server.domain.auth.exception.AuthCodeAlreadyCertifyException
import com.onboard.server.domain.auth.exception.AuthCodeOverLimitException
import com.onboard.server.domain.auth.exception.WrongEmailException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AuthCodeTest : DescribeSpec({
    describe("certify") {
        val correctEmail = "alsdl0629@dsm.hs.kr"

        context("전달된 정보가 모두 일치하면") {
            val authCode = createAuthCode(email = correctEmail)

            it("인증에 성공한다") {
                shouldNotThrowAny {
                    authCode.certify(correctEmail)
                }
            }
        }

        context("매개변수의 email과 일치하지 않으면") {
            val authCode = createAuthCode(email = correctEmail)
            val wrongEmail = "alsdl0629@gmail.com"

            it("인증에 실패한다") {
                shouldThrow<WrongEmailException> {
                    authCode.certify(wrongEmail)
                }
            }
        }

        context("이미 인증된 코드이면") {
            val alreadyCertifyAuthCode = createAuthCode(email = correctEmail, isVerified = true)

            it("예외가 발생한다") {
                shouldThrow<AuthCodeAlreadyCertifyException> {
                    alreadyCertifyAuthCode.certify(correctEmail)
                }
            }
        }
    }

    describe("generateRandomCode") {
        it("6자리의 무작위 숫자를 반환한다") {
            val randomCode = AuthCode.generateRandomCode()

            randomCode.length shouldBe CODE_LENGTH
        }
    }

    describe("checkMaxRequestLimit") {
        context("최대 요청 횟수를 넘으면") {
            val count = MAX_REQUEST_LIMIT

            it("유효 시간이 지날 때 까지 인증을 더 이상 할 수 없다") {
                shouldThrow<AuthCodeOverLimitException> {
                    AuthCode.checkMaxRequestLimit(count)
                }
            }
        }
    }
})
