package com.onboard.server.domain.guide.service

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.GuideElementRequest
import com.onboard.server.domain.guide.controller.dto.UpdateGuideRequest
import com.onboard.server.domain.guide.domain.Guide
import com.onboard.server.domain.guide.domain.GuideElementJpaRepository
import com.onboard.server.domain.guide.domain.GuideJpaRepository
import com.onboard.server.domain.guide.exception.CannotCommandGuideException
import com.onboard.server.domain.guide.exception.CannotDuplicateSequenceException
import com.onboard.server.domain.guide.exception.GuideNotFoundException
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
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

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
    private lateinit var guideRepository: GuideJpaRepository

    @Autowired
    private lateinit var guideElementJpaRepository: GuideElementJpaRepository

    val temporarySubject = Subject(-1)

    init {
        this.afterTest {
            guideElementJpaRepository.deleteAllInBatch()
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
                            imageUrl = "imageUrl",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 2,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            imageUrl = "imageUrl",
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
                            imageUrl = "imageUrl",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 2,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            imageUrl = "imageUrl",
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
                            imageUrl = "imageUrl",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 2,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            imageUrl = "imageUrl",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        )
                    )
                )

                it("가이드를 생성할 수 없다") {
                    shouldThrow<CannotCommandGuideException> {
                        guideService.create(temporarySubject, request)
                    }
                }
            }

            context("가이드 요소의 순서에 중복이 발생하면") {
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
                            imageUrl = "imageUrl",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        ),
                        GuideElementRequest(
                            sequence = 1,
                            emoji = "이모지2",
                            guideElementTitle = "이건 버튼입니다.",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.",
                            imageUrl = "imageUrl",
                            shape = "가이드 박스 모양",
                            width = 100,
                            length = 100
                        )
                    )
                )

                val subject = Subject(savedTeam.id)

                it("가이드를 생성할 수 없다") {
                    shouldThrow<CannotDuplicateSequenceException> {
                        guideService.create(subject, request)
                    }
                }
            }
        }

        this.describe("modify") {
            context("가이드 정보를 받으면") {
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

                val savedGuide = guideRepository.save(
                    Guide(
                        service = savedService,
                        title = "title",
                        path = "path"
                    )
                )

                val request = UpdateGuideRequest(
                    guideTitle = "홈 화면이 업데이트되었습니다.",
                    path = "/home",
                )

                val subject = Subject(savedTeam.id)

                it("가이드를 수정한다") {
                    guideService.modify(subject, savedGuide.id, request)

                    guideRepository.findByIdOrNull(savedGuide.id)?.let {
                        it.getTitle shouldBe "홈 화면이 업데이트되었습니다."
                        it.getPath shouldBe "/home"
                    }

                }
            }

            context("수정할 가이드를 찾지 못하면") {
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

                guideRepository.save(
                    Guide(
                        service = savedService,
                        title = "title",
                        path = "path"
                    )
                )

                val request = UpdateGuideRequest(
                    guideTitle = "홈 화면이 업데이트되었습니다.",
                    path = "/home",
                )

                val subject = Subject(savedTeam.id)

                it("가이드를 수정할 수 없다") {
                    shouldThrow<GuideNotFoundException> {
                        guideService.modify(subject, 0, request)
                    }
                }
            }

            context("자신이 만든 가이드가 아니면") {
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

                val savedGuide = guideRepository.save(
                    Guide(
                        service = savedService,
                        title = "title",
                        path = "path"
                    )
                )

                val request = UpdateGuideRequest(
                    guideTitle = "홈 화면이 업데이트되었습니다.",
                    path = "/home",
                )

                it("가이드를 수정할 수 없다") {
                    shouldThrow<CannotCommandGuideException> {
                        guideService.modify(temporarySubject, savedGuide.id, request)
                    }
                }
            }
        }
    }
}
