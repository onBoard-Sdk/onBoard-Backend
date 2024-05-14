package com.onboard.server.domain.service.domain


import com.onboard.server.domain.service.exception.ServiceCannotModifyException
import com.onboard.server.domain.team.domain.Subject
import com.onboard.server.domain.team.domain.Team
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
class Service(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    val team: Team,

    @Column(nullable = false, columnDefinition = "varchar(50)")
    private var name: String,

    @Column(nullable = false)
    private var logoImageUrl: String,

    @Column(nullable = false, unique = true)
    private var serviceUrl: String,
) : BaseEntity() {
    val getName
        get() = name

    val getLogoImageUrl
        get() = logoImageUrl

    val getServiceUrl
        get() = serviceUrl

    fun modify(subject: Subject, name: String, logoImageUrl: String, serviceUrl: String) {
        checkMine(subject)
        this.name = name
        this.logoImageUrl = logoImageUrl
        this.serviceUrl = serviceUrl
    }

    fun checkMine(subject: Subject) {
        if (this.team.id != subject.id) throw ServiceCannotModifyException
    }
}
