package com.onboard.server.domain.service.service

import com.onboard.server.domain.service.controller.dto.CreateServiceRequest
import com.onboard.server.domain.service.controller.dto.ModifyServiceRequest
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.domain.service.domain.ServiceRepository
import com.onboard.server.domain.service.exception.ServiceNotFoundException
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
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
                val savedTeam = teamRepository.save(
                    Team(
                        email = "email",
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl"
                    )
                )

                val subject = Subject(savedTeam.id)

                it("서비스 생성에 성공한다") {
                    serviceService.create(subject, request)
                        .apply {
                            serviceId shouldNotBe 0
                        }
                }
            }

            context("서비스를 생성하는 팀의 정보를 찾지 못하면") {
                it("서비스 생성에 실패한다") {
                    shouldThrow<TeamNotFoundException> {
                        serviceService.create(temporarySubject, request)
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
                        name = request.name,
                        logoImageUrl = request.logoImageUrl,
                        serviceUrl = request.serviceUrl,
                    )
                )

                val subject = Subject(savedService.id)

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
                val savedTeam = teamRepository.save(
                    Team(
                        email = "email",
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl"
                    )
                )

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
                val savedTeam = teamRepository.save(
                    Team(
                        email = "email",
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl"
                    )
                )
                val subject = Subject(savedTeam.id)

                it("서비스를 삭제할 수 없다") {
                    shouldThrow<ServiceNotFoundException> {
                        serviceService.remove(subject, 1)
                    }
                }
            }
        }

        this.describe("getAll") {
            val savedTeam = teamRepository.save(
                Team(
                    email = "email",
                    password = "password",
                    name = "name",
                    logoImageUrl = "logoImageUrl"
                )
            )

            serviceRepository.saveAll(
                listOf(
                    Service(
                        team = savedTeam,
                        name = "name1",
                        logoImageUrl = "logoImageUrl1",
                        serviceUrl = "serviceUrl1",
                    ),
                    Service(
                        team = savedTeam,
                        name = "name2",
                        logoImageUrl = "logoImageUrl2",
                        serviceUrl = "serviceUrl2",
                    )
                )
            )

            val subject = Subject(savedTeam.id)

            it("자신의 모든 서비스를 가져온다") {
                val result = serviceService.getAll(subject)

                result.services.apply {
                    this[0].name shouldBe "name1"
                    this[0].logoImageUrl shouldBe "logoImageUrl1"
                    this[0].serviceUrl shouldBe "serviceUrl1"

                    this[1].name shouldBe "name2"
                    this[1].logoImageUrl shouldBe "logoImageUrl2"
                    this[1].serviceUrl shouldBe "serviceUrl2"
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
