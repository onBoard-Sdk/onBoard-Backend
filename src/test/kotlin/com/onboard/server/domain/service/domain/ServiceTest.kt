package com.onboard.server.domain.service.domain

import com.onboard.server.domain.service.createService
import com.onboard.server.domain.service.exception.ServiceCannotModifyException
import com.onboard.server.domain.team.createTeam
import com.onboard.server.domain.team.domain.Subject
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ServiceTest : DescribeSpec({
    val temporarySubject = Subject(-1)
    describe("modify") {
        context("본인이 생성한 서비스가 맞다면") {
            val team = createTeam()
            val service = createService(team)

            it("서비스를 수정한다") {
                service.modify(
                    subject = Subject(team.id),
                    name = "onBoardV2",
                    logoImageUrl = "logoImageUrlV2",
                    serviceUrl = "serviceUrlV2",
                )

                service.apply {
                    getName shouldBe "onBoardV2"
                    getLogoImageUrl shouldBe "logoImageUrlV2"
                    getServiceUrl shouldBe "serviceUrlV2"
                }
            }
        }

        context("본인이 생성한 서비스가 아니라면") {
            val team = createTeam()
            val service = createService(team)

            it("서비스를 수정할 수 없다") {
                shouldThrow<ServiceCannotModifyException> {
                    service.modify(
                        subject = temporarySubject,
                        name = "onBoardV2",
                        logoImageUrl = "logoImageUrl",
                        serviceUrl = "serviceUrl",
                    )
                }
            }
        }
    }

    describe("checkMine") {
        val team = createTeam()
        val service = createService(team)

        context("본인이 생성한 서비스가 맞다면") {
            it("다음 프로세스를 진행할 수 있다") {
                shouldNotThrowAny {
                    service.checkMine(Subject(team.id))
                }
            }
        }

        context("본인이 생성한 서비스가 아니라면") {
            it("예외 발생한다") {
                shouldThrow<ServiceCannotModifyException> {
                    service.checkMine(temporarySubject)
                }
            }
        }
    }
})
