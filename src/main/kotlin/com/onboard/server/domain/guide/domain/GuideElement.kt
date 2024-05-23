package com.onboard.server.domain.guide.domain

import com.onboard.server.domain.guide.exception.CannotDuplicateSequenceException
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
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val guide: Guide,

    @Column(nullable = false)
    private var sequence: Int,

    private var summary: String?,

    @Column(nullable = false)
    private var title: String,

    @Column(nullable = false)
    private var description: String,

    private var guideElementImageUrl: String?,

    @Column(nullable = false)
    private var shape: String,

    @Column(nullable = false)
    private var width: Int,

    @Column(nullable = false)
    private var length: Int,
) : BaseEntity() {
    companion object {
        fun checkSequenceUnique(sequences: List<Int>) {
            if (sequences.size != sequences.distinct().size)
                throw CannotDuplicateSequenceException
        }
    }
}
