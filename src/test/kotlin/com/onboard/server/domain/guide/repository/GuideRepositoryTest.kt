package com.onboard.server.domain.guide.repository

import com.onboard.server.domain.guide.domain.Guide
import com.onboard.server.domain.guide.domain.GuideElement
import com.onboard.server.domain.guide.domain.GuideElementJpaRepository
import com.onboard.server.domain.guide.domain.GuideJpaRepository
import com.onboard.server.domain.service.createService
import com.onboard.server.domain.service.repository.ServiceRepository
import com.onboard.server.domain.team.createTeam
import com.onboard.server.domain.team.repository.TeamRepository
import com.onboard.server.global.annotation.RepositoryTest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@RepositoryTest
class GuideRepositoryTest : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var guideRepository: GuideRepository

    @Autowired
    private lateinit var guideJpaRepository: GuideJpaRepository

    @Autowired
    private lateinit var guideElementJpaRepository: GuideElementJpaRepository

    @Autowired
    private lateinit var serviceRepository: ServiceRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    init {
        this.afterTest {
            guideElementJpaRepository.deleteAllInBatch()
            guideJpaRepository.deleteAllInBatch()
            serviceRepository.deleteAllInBatch()
            teamRepository.deleteAllInBatch()
        }

        this.describe("getAllByTeamId") {
            context("팀 아이디를 받으면") {
                val savedTeam = teamRepository.save(createTeam())
                val savedService = serviceRepository.save(createService(savedTeam))
                guideJpaRepository.saveAll(
                    listOf(
                        Guide(
                            service = savedService,
                            title = "홈 화면이 업데이트되었습니다.",
                            path = "/home",
                        ),
                        Guide(
                            service = savedService,
                            title = "홈 화면이 업데이트되었습니다.",
                            path = "/home",
                        ),
                    )
                )

                it("모든 가이드 목록을 반환한다") {
                    val guides = guideRepository.getAllByTeamId(savedTeam.id)
                    guides.size shouldBe 2

                    guides[0].apply {
                        guideTitle shouldBe "홈 화면이 업데이트되었습니다."
                        path shouldBe "/home"
                    }

                    guides[1].apply {
                        guideTitle shouldBe "홈 화면이 업데이트되었습니다."
                        path shouldBe "/home"
                    }
                }
            }
        }

        this.describe("getAllWithElementsByGuideId") {
            context("가이드 아이디를 받으면") {
                val savedTeam = teamRepository.save(createTeam())
                val savedService = serviceRepository.save(createService(savedTeam))
                val savedGuide = guideJpaRepository.save(
                    Guide(
                        service = savedService,
                        title = "홈 화면이 업데이트되었습니다.",
                        path = "/home",
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

                it("가이드 및 요소를 반환한다") {
                    val guidesWithElements = guideRepository.getAllWithElementsByGuideId(savedGuide.id)!!
                    guidesWithElements.guideVO.apply {
                        guideTitle shouldBe "홈 화면이 업데이트되었습니다."
                        path shouldBe "/home"
                    }

                    guidesWithElements.guideElementVOs.size shouldBe 2
                    guidesWithElements.guideElementVOs[0].apply {
                        sequence shouldBe 1
                        guideElementTitle shouldBe "이건 버튼입니다.1"
                    }

                    guidesWithElements.guideElementVOs[1].apply {
                        sequence shouldBe 2
                        guideElementTitle shouldBe "이건 버튼입니다.2"
                    }
                }
            }
        }
    }
}
