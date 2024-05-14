package com.onboard.server.domain.guide.domain

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
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    val service: Service,

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private var title: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private var path: String
) : BaseEntity()
