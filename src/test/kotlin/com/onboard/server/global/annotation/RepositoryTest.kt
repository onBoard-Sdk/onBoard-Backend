package com.onboard.server.global.annotation

import com.onboard.server.global.config.TestQueryDslConfig
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(TestQueryDslConfig::class)
@DataJpaTest
annotation class RepositoryTest
