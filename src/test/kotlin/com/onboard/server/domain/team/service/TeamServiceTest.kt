package com.onboard.server.domain.team.service

import com.onboard.server.domain.auth.createAuthCode
import com.onboard.server.domain.auth.domain.AuthCode
import com.onboard.server.domain.auth.repository.AuthCodeRepository
import com.onboard.server.domain.auth.exception.NeverCertifyException
import com.onboard.server.domain.team.controller.dto.SignUpRequest
import com.onboard.server.domain.team.createTeam
import com.onboard.server.domain.team.repository.TeamRepository
import com.onboard.server.domain.team.exception.TeamAlreadyExistsException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class TeamServiceTest : DescribeSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var teamService: TeamService

    @Autowired
    private lateinit var authCodeRepository: AuthCodeRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    init {
        this.afterTest {
            authCodeRepository.deleteAll()
            teamRepository.deleteAllInBatch()
        }

        this.describe("singUp") {
            val email = "alsdl0629@dsm.hs.kr"
            val request = SignUpRequest(
                email = email,
                password = "password",
            )

            context("인증된 이메일, 존재하지 않는 이메일이면") {
                authCodeRepository.save(
                    createAuthCode(
                        code = AuthCode.generateRandomCode(),
                        email = email,
                        isVerified = true
                    )
                )

                it("회원가입을 성공해 토큰 반환한다") {
                    teamService.singUp(request).apply {
                        accessToken shouldNotBe null
                        accessTokenExpirationTime shouldBeAfter LocalDateTime.now()
                        refreshToken shouldNotBe null
                        refreshTokenExpirationTime shouldBeAfter LocalDateTime.now()
                    }
                }
            }

            context("인증된 이메일이 아니면") {
                authCodeRepository.save(
                    createAuthCode(
                        code = AuthCode.generateRandomCode(),
                        email = email,
                    )
                )

                it("회원가입에 실패한다") {
                    shouldThrow<NeverCertifyException> {
                        teamService.singUp(request)
                    }
                }
            }

            context("이미 존재하는 이메일이면") {
                authCodeRepository.save(
                    createAuthCode(
                        code = AuthCode.generateRandomCode(),
                        email = email,
                        isVerified = true
                    )
                )

                teamRepository.save(createTeam(email))

                it("회원가입에 실패한다") {
                    shouldThrow<TeamAlreadyExistsException> {
                        teamService.singUp(request)
                    }
                }
            }
        }
    }
}
