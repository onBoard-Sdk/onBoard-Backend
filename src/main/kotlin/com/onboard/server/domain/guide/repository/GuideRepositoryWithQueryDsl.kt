package com.onboard.server.domain.guide.repository

import com.onboard.server.domain.guide.domain.QGuide.guide
import com.onboard.server.domain.guide.repository.vo.GuideVO
import com.onboard.server.domain.service.domain.QService.service
import com.onboard.server.domain.team.domain.QTeam.team
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class GuideRepositoryWithQueryDsl(
    private val jpaQueryFactory: JPAQueryFactory,
) : GuideRepository {
    override fun getAllByTeamId(teamId: Long): List<GuideVO> {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GuideVO::class.java,
                    guide.id,
                    guide.title,
                    guide.path,
                )
            ).from(guide)
            .join(guide.service, service)
            .join(service.team, team)
            .where(team.id.eq(teamId))
            .fetch()
    }
}
