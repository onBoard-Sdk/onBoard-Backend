package com.onboard.server.domain.feedback.service

import com.onboard.server.domain.feedback.controller.dto.WriteFeedbackRequest
import com.onboard.server.domain.feedback.createFeedback
import com.onboard.server.domain.feedback.repository.FeedbackRepository
import com.onboard.server.domain.service.createService
import com.onboard.server.domain.service.exception.CannotAccessServiceException
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.service.repository.ServiceRepository
import com.onboard.server.domain.team.createTeam
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.repository.TeamRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FeedbackServiceTest : DescribeSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var feedbackService: FeedbackService

    @Autowired
    private lateinit var feedbackRepository: FeedbackRepository

    @Autowired
    private lateinit var serviceRepository: ServiceRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    private val temporarySubject = Subject(-1)

    init {
        this.afterTest {
            feedbackRepository.deleteAllInBatch()
            serviceRepository.deleteAllInBatch()
            teamRepository.deleteAllInBatch()
        }

        this.describe("write") {
            context("피드백을 받으면") {
                val savedTeam = teamRepository.save(createTeam())
                val savedService = serviceRepository.save(createService(savedTeam))

                val request = WriteFeedbackRequest(
                    serviceId = savedService.id,
                    path = "/home",
                    title = "뭔가 이상해요.",
                    content = "작동이 안됩니다"
                )

                it("피드백을 작성한다") {
                    shouldNotThrowAny {
                        feedbackService.write(request)
                    }
                }
            }

            context("피드백을 남길 서비스를 찾지 못하면") {
                val wrongServiceId = 0L
                val request = WriteFeedbackRequest(
                    serviceId = wrongServiceId,
                    path = "/home",
                    title = "뭔가 이상해요.",
                    content = "작동이 안됩니다"
                )

                it("피드백을 작성할 수 없다") {
                    shouldThrow<ServiceNotFoundException> {
                        feedbackService.write(request)
                    }
                }
            }
        }

        this.describe("getAll") {
            context("서비스 아이디를 받으면") {
                val savedTeam = teamRepository.save(createTeam())
                val savedService = serviceRepository.save(createService(savedTeam))

                feedbackRepository.saveAll(
                    listOf(
                        createFeedback(savedService), createFeedback(savedService)
                    )
                )

                val subject = Subject(savedTeam.id)

                it("서비스 피드백을 반환한다") {
                    val response = feedbackService.getAll(subject, savedService.id)

                    response.feedbacks[0].apply {
                        path shouldBe "/home"
                        title shouldBe "피드백 제목"
                        content shouldBe "피드백 내용"
                    }
                }
            }

            context("자신의 서비스가 아니면") {
                val savedTeam = teamRepository.save(createTeam())
                val savedService = serviceRepository.save(createService(savedTeam))

                it("서비스 피드백을 반환받지 못한다") {
                    shouldThrow<CannotAccessServiceException> {
                        feedbackService.getAll(temporarySubject, savedService.id)
                    }
                }
            }

            context("서비스를 찾지 못하면") {
                val savedTeam = teamRepository.save(createTeam())
                val subject = Subject(savedTeam.id)

                it("서비스 피드백을 반환받지 못한다") {
                    shouldThrow<ServiceNotFoundException> {
                        val wrongServiceId = 0L
                        feedbackService.getAll(subject, wrongServiceId)
                    }
                }
            }
        }
    }
}
