package com.onboard.server.domain.guide.domain

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
class GuideElement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    val guide: Guide,

    @Column(nullable = false, unique = true, columnDefinition = "TINYINT UNSIGNED")
    private var sequence: Int,

    @Column(nullable = false)
    private var description: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private var shape: String,

    @Column(nullable = false, columnDefinition = "MEDIUMINT")
    private var width: Int,

    @Column(nullable = false, columnDefinition = "MEDIUMINT")
    private var length: Int,
) : BaseEntity()
