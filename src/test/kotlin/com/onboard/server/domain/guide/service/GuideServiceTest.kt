package com.onboard.server.domain.guide.service

import com.onboard.server.domain.guide.controller.dto.CreateGuideRequest
import com.onboard.server.domain.guide.controller.dto.GuideElementRequest
import com.onboard.server.domain.guide.controller.dto.UpdateGuideRequest
import com.onboard.server.domain.guide.domain.Guide
import com.onboard.server.domain.guide.domain.GuideElement
import com.onboard.server.domain.guide.domain.GuideElementJpaRepository
import com.onboard.server.domain.guide.domain.GuideJpaRepository
import com.onboard.server.domain.guide.exception.CannotAccessGuideException
import com.onboard.server.domain.guide.exception.CannotDuplicateSequenceException
import com.onboard.server.domain.guide.exception.GuideNotFoundException
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.service.repository.ServiceRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.repository.TeamRepository
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
                    shouldThrow<CannotAccessGuideException> {
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
                    shouldThrow<CannotAccessGuideException> {
                        guideService.modify(temporarySubject, savedGuide.id, request)
                    }
                }
            }
        }

        this.describe("getAllByTeamId") {
            context("팀 아이디를 받으면") {
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

                guideRepository.saveAll(
                    listOf(
                        Guide(
                            service = savedService,
                            title = "title1",
                            path = "path1",
                        ),
                        Guide(
                            service = savedService,
                            title = "title2",
                            path = "path2",
                        ),
                    )
                )

                val subject = Subject(savedTeam.id)

                it("가이드 목록을 반환한다") {
                    val response = guideService.getAll(subject)

                    response.guides[0].apply {
                        guideTitle shouldBe "title1"
                        path shouldBe "path1"
                    }

                    response.guides[1].apply {
                        guideTitle shouldBe "title2"
                        path shouldBe "path2"
                    }
                }
            }
        }

        this.describe("getAllGuideElements") {
            context("가이드 아이디를 받으면") {
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
                        title = "title1",
                        path = "path1",
                    )
                )

                guideElementJpaRepository.saveAll(
                    listOf(
                        GuideElement(
                            guide = savedGuide,
                            sequence = 1,
                            summary = "이모지1",
                            title = "이건 버튼입니다.1",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.1",
                            guideElementImageUrl = "imageUrl1",
                            shape = "가이드 박스 모양1",
                            width = 100,
                            length = 100
                        ),
                        GuideElement(
                            guide = savedGuide,
                            sequence = 2,
                            summary = "이모지2",
                            title = "이건 버튼입니다.2",
                            description = "버튼을 클릭하면 이벤트가 발생합니다.2",
                            guideElementImageUrl = "imageUrl2",
                            shape = "가이드 박스 모양2",
                            width = 200,
                            length = 200
                        )
                    )
                )

                val subject = Subject(savedTeam.id)

                it("가이드 및 가이드 요소 정보를 반환한다") {
                    val response = guideService.getAllGuideElements(subject, savedGuide.id)

                    response.guide.apply {
                        guideTitle shouldBe "title1"
                        path shouldBe "path1"
                    }

                    response.guideElements[0].apply {
                        sequence shouldBe 1
                        guideElementTitle shouldBe "이건 버튼입니다.1"
                    }

                    response.guideElements[1].apply {
                        sequence shouldBe 2
                        guideElementTitle shouldBe "이건 버튼입니다.2"
                    }
                }
            }
        }

        this.describe("getAllByPath") {
            context("url 경로를 받으면") {
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

                val path = "/home"
                guideRepository.saveAll(
                    listOf(
                        Guide(
                            service = savedService,
                            title = "title1",
                            path = path,
                        ),
                        Guide(
                            service = savedService,
                            title = "title2",
                            path = path,
                        ),
                    )
                )

                it("url 경로에 포함된 가이드 목록을 불러온다") {
                    val response = guideService.getAll(path)

                    response.guides[0].apply {
                        guideTitle shouldBe "title1"
                        this.path shouldBe path
                    }

                    response.guides[1].apply {
                        guideTitle shouldBe "title2"
                        this.path shouldBe path
                    }
                }
            }
        }
    }
}
