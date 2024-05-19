package com.onboard.server.domain.guide.domain

import com.onboard.server.domain.guide.exception.CannotCreateGuideException
import com.onboard.server.domain.service.domain.Service
import com.onboard.server.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Guide(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    val service: Service,

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private var title: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(50)", name = "guide_path")
    private var path: String,
) : BaseEntity() {
    fun checkCreatable(teamId: Long) {
        if (this.service.team.id != teamId) throw CannotCreateGuideException
    }
}
