package com.onboard.server.domain.guide.service

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.GuideElementRequest
import com.onboard.server.domain.guide.domain.GuideElementRepository
import com.onboard.server.domain.guide.domain.GuideRepository
import com.onboard.server.domain.guide.exception.CannotCreateGuideException
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.domain.TeamRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.booleans.shouldBeTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GuideServiceTest : DescribeSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var guideService: GuideService

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var serviceRepository: ServiceRepository

    @Autowired
    private lateinit var guideRepository: GuideRepository

    @Autowired
    private lateinit var guideElementRepository: GuideElementRepository

    val temporarySubject = Subject(-1)

    init {
        this.afterTest {
            guideElementRepository.deleteAllInBatch()
            guideRepository.deleteAllInBatch()
            serviceRepository.deleteAllInBatch()
            teamRepository.deleteAllInBatch()
        }

        this.describe("create") {
            context("가이드 요소를 받으면") {
                val savedTeam = teamRepository.save(
                    Team(
                        email = "email",
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl"
                    )
                )

                val savedService = serviceRepository.save(
                    Service(
                        team = savedTeam,
                        name = "name",
                        logoImageUrl = "logoImageUrl",
                        serviceUrl = "serviceUrl",
                    )
                )

                val request = CreateGuideRequest(
                    serviceId = savedService.id,
                    guideTitle = "홈 화면입니다.",
                    path = "/home",
                    guideElements = listOf(
                        GuideElementRequest(
                            sequence = 1,
                            emoji = "이모지",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 2,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        )
                    )
                )

                val subject = Subject(savedTeam.id)

                it("가이드를 생성한다") {
                    val guideResponse = guideService.create(subject, request)

                    (guideResponse.guideId > 0).shouldBeTrue()
                }
            }

            context("가이드를 생성할 서비스를 찾지 못하면") {
                val wrongServiceId = 0L
                val request = CreateGuideRequest(
                    serviceId = wrongServiceId,
                    guideTitle = "홈 화면입니다.",
                    path = "/home",
                    guideElements = listOf(
                        GuideElementRequest(
                            sequence = 1,
                            emoji = "이모지",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 2,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        )
                    )
                )

                it("가이드를 생성할 수 없다") {
                    shouldThrow<ServiceNotFoundException> {
                        guideService.create(temporarySubject, request)
                    }
                }
            }

            context("자신의 서비스가 아니면") {
                val savedTeam = teamRepository.save(
                    Team(
                        email = "email",
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl"
                    )
                )

                val savedService = serviceRepository.save(
                    Service(
                        team = savedTeam,
                        name = "name",
                        logoImageUrl = "logoImageUrl",
                        serviceUrl = "serviceUrl",
                    )
                )

                val request = CreateGuideRequest(
                    serviceId = savedService.id,
                    guideTitle = "홈 화면입니다.",
                    path = "/home",
                    guideElements = listOf(
                        GuideElementRequest(
                            sequence = 1,
                            emoji = "이모지",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 2,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        )
                    )
                )

                it("가이드를 생성할 수 없다") {
                    shouldThrow<CannotCreateGuideException> {
                        guideService.create(temporarySubject, request)
                    }
                }
            }
        }
    }
}
