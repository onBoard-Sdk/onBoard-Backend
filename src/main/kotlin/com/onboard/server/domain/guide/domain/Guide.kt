package com.onboard.server.domain.guide.domain

import com.onboard.server.domain.guide.exception.CannotAccessGuideException
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
    @JoinColumn(nullable = false)
    val service: Service,

    @Column(nullable = false)
    private var title: String,

    @Column(nullable = false, name = "guide_path")
    private var path: String,
) : BaseEntity() {
    fun checkMine(teamId: Long) {
        if (this.service.team.id != teamId) throw CannotAccessGuideException
    }

    fun update(title: String, path: String) {
        this.title = title
        this.path = path
    }

    val getTitle
        get() = title

    val getPath
        get() = path
}
