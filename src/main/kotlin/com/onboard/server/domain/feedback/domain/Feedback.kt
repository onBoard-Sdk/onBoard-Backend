package com.onboard.server.domain.feedback.domain

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
class Feedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val service: Service,

    @Column(nullable = false)
    val path: String,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,
) : BaseEntity()
