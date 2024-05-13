package com.onboard.server.domain.auth.service

import com.ninjasquad.springmockk.MockkBean
import com.onboard.server.domain.auth.domain.AuthCode
import com.onboard.server.domain.auth.domain.AuthCodeRepository
import com.onboard.server.domain.auth.domain.RefreshToken
import com.onboard.server.domain.auth.domain.RefreshTokenRepository
import com.onboard.server.domain.auth.exception.AuthCodeNotFoundException
import com.onboard.server.domain.auth.exception.AuthCodeOverLimitException
import com.onboard.server.domain.auth.exception.RefreshTokenNotFoundException
import com.onboard.server.domain.auth.exception.WrongAuthInfoException
import com.onboard.server.domain.team.domain.Team
import com.onboard.server.domain.team.domain.TeamRepository
import com.onboard.server.domain.team.exception.TeamAlreadyExistsException
import com.onboard.server.thirdparty.email.EmailService
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldNotBe
import io.mockk.justRun
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@SpringBootTest
class AuthServiceTest : DescribeSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var authService: AuthService

    @MockkBean
    private lateinit var emailService: EmailService

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var authCodeRepository: AuthCodeRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    private val email = "alsdl0629@dsm.hs.kr"

    init {
        this.afterTest {
            teamRepository.deleteAllInBatch()
            authCodeRepository.deleteAll()
            refreshTokenRepository.deleteAll()
        }

        this.describe("sendAuthCode") {
            justRun { emailService.send(any(), any()) }

            context("사용자의 이메일을 받으면") {
                it("해당 이메일로 무작위 인증 코드를 전송한다") {
                    authService.sendAuthCode(email)

                    verify { emailService.send(any(), any()) }
                }
            }

            context("사용자의 이메일이 이미 존재하는 이메일이면") {
                teamRepository.save(
                    Team(
                        email = email,
                        password = "password",
                        name = "name",
                        logoImageUrl = "logoImageUrl",
                    )
                )

                it("예외가 발생한다") {
                    shouldThrow<TeamAlreadyExistsException> {
                        authService.sendAuthCode(email)
                    }
                }
            }

            context("최대 요청 횟수를 넘으면") {
                val authCodes = mutableListOf<AuthCode>()
                (1..AuthCode.MAX_REQUEST_LIMIT).forEach {
                    authCodes.add(
                        AuthCode(
                            code = AuthCode.generateRandomCode(),
                            email = email
                        )
                    )
                }
                authCodeRepository.saveAll(authCodes)

                it("예외가 발생한다") {
                    shouldThrow<AuthCodeOverLimitException> {
                        authService.sendAuthCode(email)
                    }
                }
            }
        }

        this.describe("certifyAuthCode") {
            context("전달된 정보가 모두 일치하면") {
                val authCode = authCodeRepository.save(
                    AuthCode(
                        code = AuthCode.generateRandomCode(),
                        email = email
                    )
                )

                it("인증에 성공한다") {
                    shouldNotThrowAny {
                        authService.certifyAuthCode(authCode.code, authCode.email)
                    }
                }
            }

            context("매개변수로 받은 인증 코드를 찾지 못하면") {
                val wrongCode = "000000"

                it("예외가 발생한다") {
                    shouldThrow<AuthCodeNotFoundException> {
                        authService.certifyAuthCode(wrongCode, email)
                    }
                }
            }
        }

        this.describe("signIn") {
            val password = "password"

            context("이메일, 비밀번호가 일치하면") {
                teamRepository.save(
                    Team(
                        email = email,
                        password = passwordEncoder.encode(password),
                        name = "name",
                        logoImageUrl = "logoImageUrl",
                    )
                )

                it("로그인을 성공해 토큰을 반환한다") {
                    authService.signIn(email, password).apply {
                        accessToken shouldNotBe null
                        accessTokenExpirationTime shouldBeAfter LocalDateTime.now()
                        refreshToken shouldNotBe null
                        refreshTokenExpirationTime shouldBeAfter LocalDateTime.now()
                    }
                }
            }

            context("이메일로 팀을 찾지 못하면") {
                val wrongEmail = "alsdl0629@gmail.com"

                it("로그인에 실패한다") {
                    shouldThrow<WrongAuthInfoException> {
                        authService.signIn(wrongEmail, password)
                    }
                }
            }

            context("비밀번호가 일치하지 않으면") {
                val wrongPassword = "wrongPassword"

                it("로그인에 실패한다") {
                    shouldThrow<WrongAuthInfoException> {
                        authService.signIn(email, wrongPassword)
                    }
                }
            }
        }

        this.describe("reissue") {
            val refreshToken = "refreshToken"

            context("Refresh Token으로 객체를 찾으면") {
                refreshTokenRepository.save(
                    RefreshToken(
                        userId = 1L,
                        token = refreshToken
                    )
                )

                it("Access, Refresh Token을 갱신하고, 반환한다") {
                    authService.reissue(refreshToken).apply {
                        accessToken shouldNotBe null
                        accessTokenExpirationTime shouldBeAfter LocalDateTime.now()
                        this.refreshToken shouldNotBe refreshToken
                        refreshTokenExpirationTime shouldBeAfter LocalDateTime.now()
                    }
                }
            }

            context("Refresh Token으로 객체를 찾지 못하면") {
                it("토큰 재발급에 실패한다") {
                    shouldThrow<RefreshTokenNotFoundException> {
                        authService.reissue(refreshToken)
                    }
                }
            }
        }
    }
}
