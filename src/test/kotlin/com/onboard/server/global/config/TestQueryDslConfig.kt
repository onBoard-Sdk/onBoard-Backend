package com.onboard.server.global.config

import com.onboard.server.domain.guide.repository.GuideRepositoryWithQueryDsl
import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestQueryDslConfig(
    @PersistenceContext
    private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager)

    @Bean
    fun guideRepository() = GuideRepositoryWithQueryDsl(jpaQueryFactory())
}
