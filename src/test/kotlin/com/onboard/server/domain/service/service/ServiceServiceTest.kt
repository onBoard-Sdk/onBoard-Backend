package com.onboard.server.domain.service.service

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.controller.dto.ModifyServiceRequest
import com.onboard.server.domain.service.createService
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.service.exception.ServiceUrlAlreadyExistsException
import com.onboard.server.domain.team.createTeam
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

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

        val temporarySubject = Subject(-1)
        this.describe("create") {
            val request = CreateServiceRequest(
                name = "onBoard",
                logoImageUrl = "logoImageUrl",
                serviceUrl = "localhost"
            )

            context("서비스 정보를 받으면") {
                val savedTeam = teamRepository.save(createTeam())
                val subject = Subject(savedTeam.id)

                it("서비스 생성에 성공한다") {
                    serviceService.create(subject, request)
                        .apply { serviceId shouldBeGreaterThan 0 }
                }
            }

            context("서비스를 생성하는 팀의 정보를 찾지 못하면") {
                it("서비스 생성에 실패한다") {
                    shouldThrow<TeamNotFoundException> {
                        serviceService.create(temporarySubject, request)
                    }
                }
            }

            context("이미 존재하는 서비스 URL이면") {
                val savedTeam = teamRepository.save(createTeam())

                serviceRepository.save(
                    createService(team = savedTeam, serviceUrl = request.serviceUrl)
                )

                val subject = Subject(savedTeam.id)

                it("서비스를 생성할 수 없다") {
                    shouldThrow<ServiceUrlAlreadyExistsException> {
                        serviceService.create(subject, request)
                    }
                }
            }
        }

        this.describe("modify") {
            val request = ModifyServiceRequest(
                name = "onBoard",
                logoImageUrl = "logoImageUrl",
                serviceUrl = "localhost"
            )

            context("서비스 정보를 받으면") {
                val savedTeam = teamRepository.save(createTeam())

                val savedService = serviceRepository.save(
                    createService(savedTeam)
                )

                val subject = Subject(savedTeam.id)

                it("서비스 수정에 성공한다") {
                    serviceService.modify(subject, savedService.id, request)
                        .apply {
                            serviceId shouldNotBe 0
                        }
                }
            }

            context("서비스를 수정하는 팀의 정보를 찾지 못하면") {
                it("서비스 수정에 실패한다") {
                    shouldThrow<TeamNotFoundException> {
                        serviceService.modify(temporarySubject, 0, request)
                    }
                }
            }

            context("수정하고자 하는 서비스를 찾지 못하면") {
                val savedTeam = teamRepository.save(createTeam())

                val subject = Subject(savedTeam.id)

                it("서비스 수정에 실패한다") {
                    shouldThrow<ServiceNotFoundException> {
                        serviceService.modify(subject, 0, request)
                    }
                }
            }
        }

        this.describe("remove") {
            context("자신이 만든 서비스이면") {
                val savedTeam = teamRepository.save(createTeam())

                val savedService = serviceRepository.save(
                    createService(savedTeam)
                )

                val subject = Subject(savedTeam.id)

                it("서비스를 삭제한다") {
                    serviceService.remove(subject, savedService.id)

                    serviceRepository.findByIdOrNull(savedService.id).apply {
                        this shouldBe null
                    }
                }
            }

            context("서비스를 삭제하는 팀의 정보를 찾지 못하면") {
                it("서비스를 삭제할 수 없다") {
                    shouldThrow<TeamNotFoundException> {
                        serviceService.remove(temporarySubject, 1)
                    }
                }
            }

            context("삭제할 서비스를 찾지 못하면") {
                val savedTeam = teamRepository.save(createTeam())
                val subject = Subject(savedTeam.id)

                it("서비스를 삭제할 수 없다") {
                    shouldThrow<ServiceNotFoundException> {
                        serviceService.remove(subject, 1)
                    }
                }
            }
        }

        this.describe("getAll") {
            val savedTeam = teamRepository.save(createTeam())

            val savedAllServices = serviceRepository.saveAll(
                listOf(
                    createService(team = savedTeam, serviceUrl = "localhost1"),
                    createService(team = savedTeam, serviceUrl = "localhost2"),
                    createService(team = savedTeam, serviceUrl = "localhost3")
                )
            )

            val subject = Subject(savedTeam.id)

            it("자신의 모든 서비스를 가져온다") {
                val result = serviceService.getAll(subject)

                repeat(savedAllServices.size) {
                    result.services[it].apply {
                        name shouldBe "onBoard"
                        logoImageUrl shouldBe "default.jpg"
                        serviceUrl shouldBe "localhost${it + 1}"
                    }
                }
            }

            context("팀을 찾지 못하면") {
                it("서비스 목록을 조회할 수 없다") {
                    shouldThrow<TeamNotFoundException> {
                        serviceService.getAll(temporarySubject)
                    }
                }
            }
        }
    }
}
