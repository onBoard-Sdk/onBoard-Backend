package com.onboard.server.domain.guide.repository

import com.onboard.server.domain.guide.domain.QGuide.guide
import com.onboard.server.domain.guide.domain.QGuideElement.guideElement
import com.onboard.server.domain.guide.repository.vo.AllGuidesWithElementsVO
import com.onboard.server.domain.guide.repository.vo.GuideElementVO
import com.onboard.server.domain.guide.repository.vo.GuideVO
import com.onboard.server.domain.service.domain.QService.service
import com.onboard.server.domain.team.domain.QTeam.team
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
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

    override fun getAllWithElementsByGuideId(guideId: Long): AllGuidesWithElementsVO? {
        return jpaQueryFactory
            .select(guide)
            .from(guide)
            .join(guideElement)
            .on(guide.id.eq(guideElement.guide.id))
            .where(guide.id.eq(guideId))
            .transform(
                groupBy(guide.id)
                    .`as`(
                        Projections.constructor(
                            AllGuidesWithElementsVO::class.java,
                            Projections.constructor(
                                GuideVO::class.java,
                                guide.id,
                                guide.title,
                                guide.path
                            ),
                            list(
                                Projections.constructor(
                                    GuideElementVO::class.java,
                                    guideElement.id,
                                    guideElement.sequence,
                                    guideElement.summary,
                                    guideElement.title,
                                    guideElement.description,
                                    guideElement.guideElementImageUrl,
                                    guideElement.shape,
                                    guideElement.width,
                                    guideElement.length
                                )
                            )
                        )
                    )
            )[guideId]
    }
}
