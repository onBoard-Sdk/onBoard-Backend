package com.onboard.server.domain.feedback.service

import com.onboard.server.domain.feedback.controller.dto.WriteFeedbackRequest
import com.onboard.server.domain.feedback.repository.FeedbackRepository
import com.onboard.server.domain.service.createService
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.service.repository.ServiceRepository
import com.onboard.server.domain.team.createTeam
import com.onboard.server.domain.team.repository.TeamRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
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
    }
}
