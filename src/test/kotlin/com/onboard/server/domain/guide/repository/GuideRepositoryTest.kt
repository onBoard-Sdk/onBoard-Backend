package com.onboard.server.domain.guide.repository

import com.onboard.server.domain.guide.domain.Guide
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
    private lateinit var serviceRepository: ServiceRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    init {
        this.afterTest {
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
    }
}
