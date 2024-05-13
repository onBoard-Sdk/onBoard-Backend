package com.onboard.server.domain.service.service

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ServiceServiceTest : DescribeSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var serviceService: ServiceService

    @Autowired
    private lateinit var serviceRepository: ServiceRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    init {
        this.afterTest {
            serviceRepository.deleteAllInBatch()
            teamRepository.deleteAllInBatch()
        }

        this.describe("create") {
            val subject = Subject(1L)

            val request = CreateServiceRequest(
                name = "onBoard",
                logoImageUrl = "logoImageUrl",
                serviceUrl = "localhost"
            )

            context("서비스 정보를 받으면") {
                teamRepository.save(
                    Team(
                        email = "email",
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl"
                    )
                )

                it("서비스 생성에 성공한다") {
                    serviceService.create(request, subject)
                        .apply {
                            serviceId shouldNotBe 0
                        }
                }
            }

            context("서비스를 생성하는 팀의 정보를 찾지 못하면") {
                it("예외가 발생한다") {
                    shouldThrow<TeamNotFoundException> {
                        serviceService.create(request, subject)
                    }
                }
            }
        }
    }
}
